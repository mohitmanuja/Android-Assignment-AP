# Image Loading Assignment 

## Overview

The Image Grid App is an Android application that efficiently loads and displays images in a scrollable grid. This project demonstrates asynchronous image loading, caching mechanisms, and smooth scrolling without using any third-party image loading libraries.

## Features

- **Image Grid**: Displays images in a 3-column grid with center-cropped images.
- **Asynchronous Image Loading**: Loads images asynchronously to ensure smooth scrolling.
- **Caching**: Implements both memory and disk caching for efficient image retrieval.
- **Error Handling**: Gracefully handles network errors and image loading failures with informative error messages or placeholders.
- **Smooth Scrolling**: Ensures that scrolling is smooth, even when quickly scrolling through many images.

## Requirements

- Android Studio (latest version)
- Kotlin

## Implementation Details

### Architecture

- **MainActivity**: Initializes the RecyclerView and sets up the adapter.
- **HomeAdapter**: Manages the list of images and binds view holders.
- **MediaViewHolder**: Handles the binding of individual items, including image loading and caching.
- **CachingUtil**: Utility class for handling memory and disk caching.

### Image URL Construction

The image URLs are constructed using the provided formula. 

### Caching Strategy

- **Memory Cache**: Uses `LruCache` to cache images in memory for quick access.
- **Disk Cache**: Stores images on the device's internal storage for persistent caching.

## Getting Started

### Prerequisites

- Ensure you have the latest version of Android Studio installed.
- Clone this repository to your local machine.

### Installation

1. Open Android Studio.
2. Select "Open an existing Android Studio project".
3. Navigate to the cloned repository and select it.
4. Let Android Studio build the project.

### Running the Application

1. Connect an Android device or start an emulator.
2. Click the "Run" button in Android Studio.
3. The application should launch on the selected device/emulator.

