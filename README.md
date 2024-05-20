# Image Loading Assignment 

## Overview

The Image Grid App is an Android application that efficiently loads and displays images in a scrollable grid. This project demonstrates asynchronous image loading, caching mechanisms, and smooth scrolling without using any third-party image-loading libraries.

## Features

### 1. Simple and Concise API
   - Load images into ImageViews just like any other image-loading library.

## Things I have done to optimize performance 

### 2. In memory Caching and Disk management 
   - Images are automatically cached in memory and disk to improve performance and reduce redundant network calls.
   - Efficient caching strategy (LRU Cache) ensures that frequently and recently used images are readily available.

### 3. Downscaling
   - Optionally downscale images to reduce in-memory usage and improve loading times.

### 4. Memory Management
   - Efficient memory management to limit the memory footprint of cached images.
   - Automatically evicts least-recently-used images from memory cache to prevent out-of-memory errors.

### 5. View Lifecycle Handling/Cancellation 
   - Listens to the view lifecycle events to cancel image loading tasks when the associated ImageView is detached from the window.
   - Prevents memory leaks and improves efficiency by cancelling tasks for detached views.

### 6. Error Handling
   - Gracefully handles errors such as IOExceptions during image loading, ensuring robustness and reliability.
   - Provides clear error messages and fallback options to enhance user experience.

### 7. Cache Size Management
   - Manages cache size to prevent it from growing too large.
   - Automatically evicts the least recently used images to free up memory and maintain optimal performance.

### 8. ThreadPool Executor 
   - A fixed-size Thread Pool Executor is employed to prevent network bandwidth congestion caused by an excessive number of concurrent network requests.

### 8. Background Task:
   - All I/O (disk read and write) and network operations are executed on background threads.

## Requirements

- Android Studio (latest version)
- Kotlin

## Showcase 

### Flow Diagram 
![Image](./media/flowdiagram.png)

### Video 1: First time loading with Internet and switching off the Internet (Showing cached response and cached images from disk) 
[Click here to watch](https://drive.google.com/file/d/1pXTg0DE6XAPtams1Y6JxFnnGvmBnqW5Y/view?usp=sharing)


### Video 2: First time loading without Internet and switching on the Internet later (Showing network error handling and retry) 
[Click here to watch](https://drive.google.com/file/d/1kJWLYkS_Zdv4T9K30NW3N9Ga2WHoBHLL/view?usp=sharing)

### Download Debug APK 
[Click here to download](https://drive.google.com/file/d/13nENpdUSiniNR5Sqw_LBrTVyVfttbAz3/view?usp=sharing)



## Implementation Details

```
ImageLoader.with(context)
    .load(imageUrl)
    .into(imageView)
    .placeholder(R.drawable.placeholder)
    .error(R.drawable.error)
    .downscale(200, 150)
    .load()
```


### Architecture

- **MainActivity**: Initializes the RecyclerView and sets up the adapter.
- **HomeAdapter**: Manages the list of images and binds view holders.
- **MediaViewHolder**: Handles the binding of individual items, including image loading and caching.
- **CachingUtil**: A utility class for handling memory and disk caching.
- **ImageLoader**: A utility class for handling image-loading tasks. It provides a builder pattern for configuring image loading options, such as placeholders, error images, and downscaling options. It also manages the loading of images asynchronously, handling caching, and canceling ongoing loading tasks when necessary.



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

