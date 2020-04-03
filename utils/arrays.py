import numpy as np

def normalize_array(array_in:np.array) -> np.array:

    array_min = int(np.amin(array_in))
    array_max = int(np.amax(array_in))
    array_range = array_max - array_min
    normalized_array = (array_in - array_min) / array_range

    return normalized_array
