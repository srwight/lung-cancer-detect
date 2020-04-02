'''
This module provides generators to generate arrays for CT scans in directories,
as well as generators to collect cubes of data from the arrays.
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

def get_vox_coords(mm_coords:tuple, mm_per_vox:tuple, mm_origin:tuple, padding:int=0) -> tuple:
    vox_coords = tuple(
        int((coord - origin) / spacing) + padding \
            for \
                coord, \
                spacing, \
                origin \
            in zip(
                mm_coords,
                mm_per_vox, 
                mm_origin
        )
    )
    return vox_coords

def get_r_prism(source:np.array, zyx_sides:tuple, zyx_corner:tuple) -> np.array:
    if any((side + corner) > shape for side, corner, shape in zip(zyx_sides, zyx_corner, source.shape)):
        raise IndexError("Input array is not big enough to collect sample.")

    cube = {
        "z_start":zyx_corner[0],
        "z_end":zyx_corner[0] + zyx_sides[0],
        "y_start":zyx_corner[1],
        "y_end":zyx_corner[1] + zyx_sides[1],
        "x_start":zyx_corner[2],
        "x_end":zyx_corner[2] + zyx_sides[2]
    }

    ret_cube = source[
        cube['z_start']:cube['z_end'],
        cube['y_start']:cube['y_end'],
        cube['x_start']:cube['x_end']
    ]

    return ret_cube

def rotate_prism(arr_in:np.array, rotate:float, axes:list) -> np.array:
    # Rotate by some fraction of rotate degrees
    out_array = None
    for axis in axes:
        degrees = rotate * random() * (randint(0,1) * 2 - 1)
        if axis not in (0,1,2):
            raise IndexError('Invalid Axis')
        rotation_axes = tuple(a for a in [0,1,2] if a != axis )
        out_array = ndimage.rotate(arr_in, degrees, rotation_axes, reshape=False)
    return out_array
    
def scan_from_file(filename:str):
    image = ReadImage(filename)
    
    # get array and metadata to pass on
    array = GetArrayFromImage(image)
    spacing = image.GetSpacing()[::-1]
    origin = image.GetOrigin()[::-1]
    
    # Normalize Array
    array_min = int(np.amin(array))
    array_max = int(np.amax(array))
    array_range = array_max - array_min
    normalized_array = (array - array_min) / array_range

    # return array and metadata
    return normalized_array, spacing, origin, filename

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
    for filename in files:
        yield scan_from_file(f'{dirname}/{filename}')

generate_data_nested_dirs = nested_dirs(generate_data_same_dir)

def get_cube_at_point(
    scan,
    zyx_coords:tuple,
    mm_sidelength:float,
    target_size,
    rotate:float = 0,
    axes:list = [],
    offset:float = 0) -> np.array:
    
    source, zyx_spacing, zyx_origin = scan[0:3]
    # Pad source so that it we have at least 2 sidelengths + 5mm
    padding = int((mm_sidelength * 2 + 5) / min(zyx_spacing))
    source = np.pad(source, padding, 'edge')

    if offset > 1:
        raise(IndexError, "offset must be between 0.0 and 1.0")

    if offset > 0.5:
        warnings.warn('offset > 0.5 may result in target being outside of sample')

    # if offset:
    #     placement = 1 + offset * randint(-1,1) * random()
    # else:
    #     placement = 1
    
    # Convert mm coordinates to voxel coordinates
    zyx_vox_coords = get_vox_coords(zyx_coords, zyx_spacing, zyx_origin, padding)

    # sidelength in voxels of the cube I'm looking for, multiplied by 2 to prepare for augmentation.
    zyx_r_edges = tuple(int(mm_sidelength / spacing * 2 + 1) for spacing in zyx_spacing)

    # sets the top left back corner of the cube
    zyx_corner = tuple(coord - edge for coord, edge in zip(zyx_vox_coords, zyx_r_edges))

    zyx_sidelen = tuple(radius * 2 for radius in zyx_r_edges)

    aug_array = get_r_prism(source, zyx_sidelen, zyx_corner)

    pre_aug_size = target_size * 4

    # When we zoom the cube to the target size, we'll use these values to do so.
    scaler = tuple(pre_aug_size / current for current in aug_array.shape)

    # Zooms the cube to the target size.
    aug_array = ndimage.zoom(aug_array, scaler)
    
    if rotate:
        aug_array = rotate_prism(aug_array, rotate, axes)
    
    offset_vox = int(target_size * offset)
    zyx_corner = tuple(
        int(0.375 * edge + offset_vox * random() * (randint(0,2) - 1)) \
        for edge in aug_array.shape)

    cube_array = get_r_prism(aug_array, (target_size, target_size, target_size), zyx_corner)

    return cube_array

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
    return cube, zyx_mm_coords

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
            yield cube, np.array([0,1])
            cancer = True
        if not cancer:
            clean_files.append(path)
            while nodule - no_nodule > 0:
                cube = np.expand_dims(get_random_cube(scan, target_size, max_diameter)[0],3)
                no_nodule += 1
                yield cube, np.array([1,0])
    if not clean_files:
        warnings.warn('There are no clean scans. Data is not balanced.')
    clean_scans = infinite_looper(clean_files)
    while nodule - no_nodule > 0:
        scan = scan_from_file(next(clean_scans))
        no_nodule += 1
        # This line gets a random cube from the scan, then adds a dimension.
        cube = np.expand_dims(get_random_cube(scan, target_size, max_diameter)[0], 3)
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
        batch_list.append(cube[0])
        y_list.append(cube[1])
        while len(batch_list) < batch_size:
            try:
                cube = next(cubes)
            except StopIteration:
                break
            batch_list.append(cube[0])
            y_list.append(cube[1])
                                                                                                                                                                                                                                                                                                                            yield np.stack(batch_list), np.stack(y_list)
        batch_list = []
        y_list = []

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
    # cube = get_random_cube(scan, (24,64,64)[0],5)
    # print(f'\nRandome cube with 5mm edge length:\n{cube}')
    # print(f'\n\nCube Shape: {cube.shape}')
    dirs = list_dir('./data')
    for item in dirs:
        print(item)