import os

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

