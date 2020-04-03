import numpy as np
from random import randint, random

def normalize_array(array_in:np.array) -> np.array:

    array_min = int(np.amin(array_in))
    array_max = int(np.amax(array_in))
    array_range = array_max - array_min
    normalized_array = (array_in - array_min) / array_range

    return normalized_array

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
