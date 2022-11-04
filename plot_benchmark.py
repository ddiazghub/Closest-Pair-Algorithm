#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Algorithms and Complexity                               August 08, 2022
IST4310
Prof. M Diaz-Maldonado
Name: David Eduardo Díaz de Moya

Synopsis:
Plots the average number of comparisons and the average elapsed time
as a function of the input size.
"""

# imports the needed methods from numerical python and matplotlib library
import numpy as np
import os
import sys
from matplotlib import pyplot as plt

# Checks that the experimental data actually exists
if not os.path.isdir("output"):
    print("Experimental data not found. Make sure to run the closest pair java program before this one.", file=sys.stderr)
    sys.exit(1)

""" imports the experimental data """
data = {
    "recursive": np.loadtxt("output/recursive.txt", skiprows=1).T,
    "brute force": np.loadtxt("output/brute_force.txt", skiprows=1).T,
}

# Loops through the different datasets
for case, values in data.items():
    # unpackins the input size, the number of comparisons, and the elapsed time
    size = values[0]
    etime, comps = values[-2:]
    
    """ visualization """

    # closes all files and enables interactive plotting
    plt.close('all')
    plt.ion()
    # creates a figure and a set of axes
    fig, ax = plt.subplots()

    # plots the expected (theoretical) time as a function of size
    ax.loglog(size, size if case == "recursive" else size**2,    color='black', linestyle='--', label="linear" if case == "recursive" else "quadratic")
    # plots the elapsed time as a function of size
    ax.loglog(size, etime, color='black',  linestyle='', marker='o', markersize=12, label='elapsed-time')
    # plots the number of comparisons as a function of size
    ax.loglog(size, comps, color='red',   linestyle='', marker='*', markersize=12, label='comparisons')
    ax.set_xlabel('input size, N')
    ax.set_ylabel('time, t')
    ax.set_title(f'{case} algorithm')
    ax.legend()

    # exports the figure in PNG with a resolution of 600 DPI
    fig.savefig(f"output/{case}.png", dpi=600)