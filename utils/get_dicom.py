from SimpleITK import ImageSeriesReader, GetArrayFromImage
import numpy as np
import os, magic
from .decorators import nested_dirs
from .arrays import normalize_array

def check_dir_for_dicom(directory:str='.') -> bool:
    dir_listing = (f'{directory}/{filename}' for filename in os.listdir(directory) if not os.path.isdir(f'{directory}/{filename}'))
    dir_check = (magic.from_file(filename) == 'DICOM medical imaging data' for filename in dir_listing)
    return any(dir_check)

def get_series_from_directory(directory:str = '.') -> tuple:
    
    reader = ImageSeriesReader()
    dicom_names = reader.GetGDCMSeriesFileNames(directory)

    reader.SetFileNames(dicom_names)

    image = reader.Execute()
    array = GetArrayFromImage(image)
    spacing = image.GetSpacing()[::-1]
    origin = image.GetOrigin()[::-1]

    normalized_array = normalize_array(array)

    return normalized_array, spacing, origin, directory

@nested_dirs
def generate_series_from_directories(rootdir:str = '.'):
    dirlist = filter(lambda x: os.path.isdir(f'{rootdir}/{x}'), os.listdir(rootdir))
    dirlist = (f'{rootdir}/{x}' for x in dirlist)
    dirs = filter(check_dir_for_dicom, dirlist)
    for directory in dirs:
        yield get_series_from_directory(f'{directory}')

if __name__ == "__main__":
    # scan = get_series_from_directory('data/russian_data/RLAD31D006-11315_RLS5A09001KDC6-K00008714/CT/Chest HCT')
    # for item in scan[::-1]:
    #     print(item)
    #     if hasattr(item, 'shape'):
    #         print(item.shape)

    dicom = generate_series_from_directories('./data/russian_data/Dicom')
    for scan in dicom:
        print(scan[0].shape)

