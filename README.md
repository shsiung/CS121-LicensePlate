VehiCool - Introduction
==================
VehiCool is an app that allows car enthusiasts and travelers to keep track of all the license plates they see on the road or in their travels. Our app allows the user to quickly line-up and snap a photo of a license plate using their android device's back camera, and then tag the plate with useful information. The user can then search and view all of their stored plates to enjoy their growing collection of funny, exotic, or faraway license plates.

Using VehiCool
==================
Opening the app first loads a splash-screen that contains a logos and our names as developers. After a short delay, the splash-screen fades away to the camera preview page. The app employs fragments that allows activities to be accessed by tabs in the top bar. This allows smooth transitions between pages and appears as if no new activities are opened. We found this to feel much smoother and less disruptive to the flow of the app. 

The camera page uses an active preview from the back camera on the device and before displaying it, draws a guidance overlay onto the preview. The overlay is a translucent grey border with a clear rectangular center. The clear center has green corner markings and denotes the area of the preview that should be aligned with the plate. Upon taking a picture, the application crops the photo to only include what is inside the clear center. It then runs text recognition on the photo and  passes the photo's directory address along with any recognized text to the tagging page. 

Directory Overview
==================
- Main Directory
	- VehiCool source files (src)
	- openCV library files 
	- tesseract library files

Third Party Libraries
==================
VehiCool uses several third party libraries to a, including:

- For map displaying, we use [Google Map](https://developers.google.com/maps/documentation/android/).
- For text recognition, we use [Tesseract](https://code.google.com/p/tesseract-ocr/).
- For image processing, we use [OpenCV for Android](http://opencv.org/platforms/android.html).

License
=======

appledoc is licensed with modified BSD license. In plain language: you're allowed to do whatever you wish with the code, modify, redistribute, embed in your products (free or commercial), but you must include copyright, terms of usage and disclaimer as stated in the license, the same way as any other BSD licensed code. You can of course use documentation generated by appledoc for your products (free or commercial), but you must attribute appledoc either in documentation itself or other appropriate place such as your website.

Copyright (c) 2009-2011, Gentle Bytes
All rights reserved.

Redistribution and use in source, binary forms and generated documentation, with or without modification, are permitted provided that the following conditions are met:

- Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.

- Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.

- Redistributions of documentation generated by appledoc must include attribution to appledoc, either in documentation itself or other appropriate media.

- Neither the name of the appledoc, Gentle Bytes nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.



The License Plate Project for CS121
