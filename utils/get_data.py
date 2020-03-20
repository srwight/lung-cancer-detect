'''
This module provides generators to generate arrays for CT scans in directories
'''
from SimpleITK import ReadImage, GetArrayFromImage, sitkFloat32
import numpy as np
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


def get_cube_at_point(source:np.array, zxy_spacing:tuple, xyz_coords:tuple, mm_sidelength:float) -> np.array:
    spacing_z, spacing_x, spacing_y = zxy_spacing
    mm_x, mm_y, mm_z = xyz_coords
    vox_center_x, vox_center_y, vox_center_z = int(mm_x / spacing_x), int(mm_y / spacing_y), int(mm_z / spacing_z)
    vox_r_edge_z, vox_r_edge_x, vox_r_edge_y = [ int((mm_sidelength / spacing) / 2 + 1) for spacing in zxy_spacing]
    vox_corner_x = vox_center_x - vox_r_edge_x
    vox_corner_y = vox_center_y - vox_r_edge_y
    vox_corner_z = vox_center_z - vox_r_edge_z

    cube = {
        "x_start":vox_corner_x,
        "x_end":vox_corner_x + vox_r_edge_x * 2,
        "y_start":vox_corner_x,
        "y_end":vox_corner_y + vox_r_edge_y * 2,
        "z_start":vox_corner_z,
        "z_end":vox_corner_z + vox_r_edge_z * 2,
    }
 
    cube_array = source[
        cube['z_start']:cube['z_end'],
        cube['x_start']:cube['x_end'],
        cube['y_start']:cube['y_end'],
    ]

    return cube_array
    


if __name__ == "__main__":
    mygen = generate_data_nested_dirs('.')
    array, spacing, origin, filename = next(mygen)
    print(f'\nShape: {array.shape}')
    print(f'\nSpacing: {spacing}')
    print(f'\nOrigin: {origin}')
    print(f'\nFilename: {filename}')
    print(f'\nCube at 5,5,5 with 2mm edge length:\n{get_cube_at_point(array,spacing,(5,5,5),2)}')
