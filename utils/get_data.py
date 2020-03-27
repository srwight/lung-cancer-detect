'''
This module provides generators to generate arrays for CT scans in directories
'''
from SimpleITK import ReadImage, GetArrayFromImage, sitkFloat32, sitkInt16
from scipy import ndimage
import numpy as np, pandas as pd
import os, warnings
from random import randint, random

def infinite_loop(genfunc):
    def looped_generator(*args, **kwargs):
        while True:
            generator = genfunc(*args, **kwargs)
            for item in generator:
                yield item
    return looped_generator

def nested_dirs(genfunc):
    def nested_func(dirname, *args, **kwargs):
        directories = filter(lambda x: os.path.isdir(f'{dirname}/{x}'), os.listdir(dirname))
        for directory in directories:
            mygen = nested_func(f'{dirname}/{directory}', *args, **kwargs)
            for item in mygen:
                yield item
        mygen = genfunc(dirname, *args, **kwargs)
        for item in mygen:
            yield item
    return nested_func

def infinite_looper(iterable):
    while True:
        for item in iterable:
            yield item

def scan_from_file(filename:str):
    image = ReadImage(filename)
    
    array = GetArrayFromImage(image)
    spacing = image.GetSpacing()[::-1]
    origin = image.GetOrigin()[::-1]

    return array, spacing, origin, filename

def generate_data_same_dir(dirname:str='.') -> tuple:
    '''
    This generator collects scans from a single directory and yields, in turn, a 
    numpy array of each scan along with its spacing scale in mm between voxels.

    Arguments:
    ==========
    dirname:str     - The name of the directory you want to generate from.

    Yields:
    ========
    tuple of data about a scan in this order:
        array
        spacing
        origin
        filename
    '''
    files=filter(lambda x: x[-3:] == 'mhd', os.listdir(dirname))
    print('I got in.')
    for filename in files:
        yield scan_from_file(f'{dirname}/{filename}')

generate_data_nested_dirs = nested_dirs(generate_data_same_dir)
# def generate_data_nested_dirs(rootdir:str='.') -> np.array:
#     '''
#     This generator collects scans from a directory tree and yields, in turn, a 
#     numpy array of each scan along with its spacing scale in mm between voxels.

#     Arguments:
#     ==========
#     dirname:str     - The name of the root directory you want to generate from.
#     '''
#     directories=filter(lambda x: os.path.isdir(x), os.listdir(rootdir))
#     for directory in directories:
#         mygen = generate_data_nested_dirs(f'{rootdir}/{directory}')
#         for item in mygen:
#             yield item
#     mygen = generate_data_same_dir(rootdir)
#     for item in mygen:
#         yield item

def get_cube_at_point(
    scan,
    zyx_coords:tuple,
    mm_sidelength:float,
    target_size,
    rotate:float = 0,
    axes:list = [],
    offset:float = 0) -> np.array:
    
    source, zyx_spacing, zyx_origin = scan[0:3]
    if offset:
        placement = 1 + offset * randint(-1,1) * random()
    else:
        placement = 1
    # Convert mm coordinates to voxel coordinates
    zyx_vox_coords = tuple(
        int((coord - origin)/spacing * placement) \
            for \
                coord, \
                spacing, \
                origin \
            in zip(
                zyx_coords,
                zyx_spacing, 
                zyx_origin
        )
    )

    # This is the edge radius in voxels of the cube I'm looking for
    zyx_r_edges = tuple(int(mm_sidelength / spacing / 2 + 1) for spacing in zyx_spacing)

    # This line sets the corner to 0 if the center is closer than the edge radius to zero
    corner_min_zero = tuple(max(coord - edge, 0) for coord, edge in zip(zyx_vox_coords, zyx_r_edges))

    # This line sets the corner to max minus 2 edge radii if the center is closer than the edge radius to the highest value.
    zyx_corner = tuple(min(cmin, shape - edge * 2) for cmin, edge, shape in zip(corner_min_zero, zyx_r_edges, source.shape))

    # This section defines all six faces off the cube.
    cube = {
        "z_start":zyx_corner[0],
        "z_end":zyx_corner[0] + zyx_r_edges[0] * 2,
        "y_start":zyx_corner[1],
        "y_end":zyx_corner[1] + zyx_r_edges[1] * 2,
        "x_start":zyx_corner[2],
        "x_end":zyx_corner[2] + zyx_r_edges[2] * 2,
    }

    # Collect the target cube array from the source array.
    cube_array = source[
        cube['z_start']:cube['z_end'],
        cube['y_start']:cube['y_end'],
        cube['x_start']:cube['x_end'],
    ]

    if rotate:
        degrees = rotate * random()
        for axis in axes:
            if axis not in (0,1,2):
                raise IndexError('Invalid Axis')
            rotation_axes = tuple(filter (lambda a: a != axis, [0,1,2]))
            cube_array = ndimage.rotate(cube_array, degrees, rotation_axes)

    # When we zoom the cube to the target size, we'll use these values to do so.
    scaler = tuple(target / current for target, current in zip(target_size, cube_array.shape))

    # Zooms the cube to the target size.
    zoom_cube = ndimage.zoom(cube_array, scaler)

    return zoom_cube

def get_random_cube(scan:tuple, target_size:tuple, diameter:float):
    '''
    TODO: Docstring
    '''
    
    source, zyx_spacing, zyx_origin = scan[0:3]
    random_buffer = (40,100,100)
    zyx_vox_coords = tuple(randint(buff,shape-40) for buff, shape in zip(random_buffer, source.shape))
    zyx_mm_coords = tuple(coord * spacing + origin for spacing, origin, coord in zip(zyx_spacing, zyx_origin, zyx_vox_coords))
    cube = get_cube_at_point(
        scan, 
        zyx_mm_coords, 
        diameter, 
        target_size)
    return cube

@infinite_loop
def generate_cubes( \
    locations:pd.DataFrame, \
    target_size:tuple, \
    headers:tuple = ('seriesuid','coordX','coordY','coordZ','diameter_mm'), \
    root_dir:str = '.',
    **kwargs):

    max_diameter = locations[headers[4]].max()
    scans = generate_data_nested_dirs(root_dir)
    nodule = 0
    no_nodule = 0
    clean_files = []
    for scan in scans:
        filename = scan[-1].rsplit('/',1)[1][:-4]
        path = scan[-1]
        cancer = False
        df_scan = locations.loc[locations[headers[0]] == filename]
        for annotation in df_scan.iterrows():
            zyx_coords = tuple(annotation[1][coord] for coord in headers[3:0:-1])
            cube = np.expand_dims(
                get_cube_at_point(
                    scan, 
                    zyx_coords, 
                    max_diameter, 
                    target_size,
                    **kwargs),
                3)
            nodule += 1
            print("[line 187]",f"nodule: {nodule}", f"no_nodule: {no_nodule}", "================", sep="\n")
            yield cube, np.array([0,1])
            cancer = True
        if not cancer:
            clean_files.append(path)
            while nodule - no_nodule > 0:
                cube = np.expand_dims(get_random_cube(scan, target_size, max_diameter),3)
                no_nodule += 1
                print("[line 195]",f"nodule: {nodule}", f"no_nodule: {no_nodule}", "================", sep="\n")
                yield cube, np.array([1,0])
    if not clean_files:
        warnings.warn('There are no clean scans. Data is not balanced.')
    clean_scans = infinite_looper(clean_files)
    while nodule - no_nodule > 0:
        scan = scan_from_file(next(clean_scans))
        no_nodule += 1
        # This line gets a random cube from the scan, then adds a dimension.
        cube = np.expand_dims(get_random_cube(scan, target_size, max_diameter), 3)
        print("[line 205]",f"nodule: {nodule}", f"no_nodule: {no_nodule}", "================", sep="\n")
        yield cube, np.array([1,0])

def generate_cube_batch(
    locations:pd.DataFrame, \
    target_size:tuple, \
    # headers:tuple = ('seriesuid','coordX','coordY','coordZ','diameter_mm'), \
    # root_dir:str = '.', \
    batch_size:int = 8,
    **kwargs):

    cubes = generate_cubes(locations, target_size, **kwargs)

    batch_list = []
    y_list=[]
    for cube in cubes:
        if len(batch_list) < batch_size:
            batch_list.append(cube[0])
            y_list.append(cube[1])
            continue
        yield np.stack(batch_list), np.stack(y_list)
        batch_list = []
        y_list = []

# def generate_false_data(locations = pd.DataFrame):

@nested_dirs
def list_dir(dirname='.'):
    yield dirname

if __name__ == "__main__":
    # mygen = generate_data_nested_dirs('.')
    # scan = next(mygen)
    # cube = get_cube_at_point(scan,(5,5,5),2,(24,64,64))
    # print(f'\nShape: {scan[0].shape}')
    # print(f'\nSpacing: {scan[1]}')
    # print(f'\nOrigin: {scan[2]}')
    # print(f'\nFilename: {scan[3]}')
    # print(f'\nCube at 5,5,5 with 2mm edge length:\n{cube}')
    # print(f'\n\nCube Shape: {cube.shape}')
    # cube = get_random_cube(scan, (24,64,64),5)
    # print(f'\nRandome cube with 5mm edge length:\n{cube}')
    # print(f'\n\nCube Shape: {cube.shape}')
    dirs = list_dir('./data')
    for item in dirs:
        print(item)