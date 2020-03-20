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
    '''
    files=filter(lambda x: x[-3:] == 'mhd', os.listdir(dirname))
    for filename in files:
        image = ReadImage(f'{dirname}/{filename}', sitkFloat32)
        spacing = image.GetSpacing()
        yield GetArrayFromImage(image), (spacing[2],*spacing[0:2]) 

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
        for item, spacing in mygen:
            yield item, spacing
    mygen = generate_data_same_dir(rootdir)
    for item, spacing in mygen:
        yield item, spacing

# def get_cube_at_point(source:np.array, zxy_spacing:tuple, mm_x:float, mm_y:float, mm_z:float, mm_sidelength:float) -> np.array:
#     spacing_z, spacing_x, spacing_y = *zxy_spacing
#     vox_center_x, vox_center_y, vox_center_z = mm_x / spacing_x, mm_y / spacing_y, mm_z / spacing_z
#     vox_side_z, vox_side_x, vox_side_y = *[ mm_sidelength / spacing for spacing in zxy_spacing]
#     vox_corner_x, vox_corner_y, vox_corner_z = 


if __name__ == "__main__":
    mygen = generate_data_nested_dirs('.')
    array, spacing = next(mygen)
    print(array.shape)
    print(spacing)
