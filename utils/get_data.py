'''
This module provides generators to generate arrays for CT scans in directories
'''
from SimpleITK import ReadImage, GetArrayFromImage, sitkFloat32
import numpy as np, pandas as pd
import os

def generate_data_same_dir(dirname:str='.') -> tuple:
    '''
    This generator collects scans from a single directory and yields, in turn, a 
    numpy array of each scan along with its spacing scale in mm between voxels.

    Arguments:
    ==========
    dirname:str     - The name of the directory you want to generate from.

    Yields:
    ========
    numpy array representation of the scan image
    tuple representing z,x,y spacing
    tuple representing z,x,y origin
    filename
    '''
    files=filter(lambda x: x[-3:] == 'mhd', os.listdir(dirname))
    for filename in files:
        image = ReadImage(f'{dirname}/{filename}', sitkFloat32)
        spacing = image.GetSpacing()
        origin = image.GetOrigin()
        yield GetArrayFromImage(image), (spacing[2],*spacing[0:2]), (origin[2], *origin[0:2]), filename[:-4]

def generate_data_nested_dirs(rootdir:str='.') -> np.array:
    '''
    This generator collects scans from a directory tree and yields, in turn, a 
    numpy array of each scan along with its spacing scale in mm between voxels.

    Arguments:
    ==========
    dirname:str     - The name of the root directory you want to generate from.
    '''
    directories=filter(lambda x: os.path.isdir(x), os.listdir(rootdir))
    for directory in directories:
        mygen = generate_data_nested_dirs(directory)
        for item in mygen:
            yield item
    mygen = generate_data_same_dir(rootdir)
    for item in mygen:
        yield item


def get_cube_at_point(source:np.array, zxy_spacing:tuple, zxy_origin:tuple, filename, zxy_coords:tuple, mm_sidelength:float) -> np.array:
    zxy_vox_coords = tuple(int((ele1 - ele3)/ele2) for ele1, ele2, ele3 in zip(zxy_coords,zxy_spacing, zxy_origin))
    zxy_r_edges = tuple(int(mm_sidelength * 2 / spacing) for spacing in zxy_spacing)
    zxy_corner = tuple(ele1 - ele2 for ele1, ele2 in zip(zxy_vox_coords, zxy_r_edges))

    cube = {
        "z_start":zxy_corner[0],
        "z_end":zxy_corner[0] + zxy_r_edges[0] * 2 + 1,
        "x_start":zxy_corner[1],
        "x_end":zxy_corner[1] + zxy_r_edges[1] * 2 + 1,
        "y_start":zxy_corner[2],
        "y_end":zxy_corner[2] + zxy_r_edges[2] * 2 + 1,
    }
 
    cube_array = source[
        cube['z_start']:cube['z_end'],
        cube['x_start']:cube['x_end'],
        cube['y_start']:cube['y_end'],
    ]

    return cube_array

def generate_true_cubes( \
    locations:pd.DataFrame, \
    headers:tuple = ('seriesuid','coordX','coordY','coordZ','diameter_mm'), \
    root_dir:str = '.'):
    
    scans = generate_data_nested_dirs(root_dir)
    for scan in scans:
        df_scan = locations.loc[locations[headers[0]] == scan[-1]]
        for annotation in df_scan.iterrows():
            zxy_coords = (annotation[1][headers[3]], annotation[1][headers[1]], annotation[1][headers[2]])
            diameter = annotation[1][headers[4]]
            yield get_cube_at_point(*scan, zxy_coords, diameter)

if __name__ == "__main__":
    mygen = generate_data_nested_dirs('.')
    scan = next(mygen)
    cube = get_cube_at_point(*scan,(5,5,5),2)
    print(f'\nShape: {scan[0].shape}')
    print(f'\nSpacing: {scan[1]}')
    print(f'\nOrigin: {scan[2]}')
    print(f'\nFilename: {scan[3]}')
    print(f'\nCube at 5,5,5 with 2mm edge length:\n{cube}')
    print(f'\n\nCube Shape: {cube.shape}')
