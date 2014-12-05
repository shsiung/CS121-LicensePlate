CS121-VehiCool
==================
VehiCool is an app that allows car enthusiasts and travelers to keep track of all the license plates they see on the road or in their travels. Our app allows the user to quickly line-up and snap a photo of a license plate using their android device's back camera, and then tag the plate with useful information. The user can then search and view all of their stored plates to enjoy their growing collection of funny, exotic, or faraway license plates.

Using VehiCool
==================
Opening the app first loads a splash-screen that contains a logos and our names as developers. After a short delay, the splash-screen fades away to the camera preview page. The app employs fragments that allows activities to be accessed by tabs in the top bar. This allows smooth transitions between pages and appears as if no new activities are opened. We found this to feel much smoother and less disruptive to the flow of the app. 

The camera page uses an active preview from the back camera on the device and before displaying it, draws a guidance overlay onto the preview. The overlay is a translucent grey border with a clear rectangular center. The clear center has green corner markings and denotes the area of the preview that should be aligned with the plate. Upon taking a picture, the application crops the photo to only include what is inside the clear center. It then runs text recognition on the photo and  passes the photo's directory address along with any recognized text to the tagging page. 

The License Plate Project for CS121
