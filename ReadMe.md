---
typora-root-url: Images
---

The initial screen is a splash screen.

![splash](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572801506949.png







![login](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572802858209.png





On Logging in to the App, the app provides a list of hillfort objects.

I have used a combination of images and text in the menu.(no reason just trying them out) .

I tried to use tooltiptext, to provide a hint as to the object of each icon once the user hovered over the icon.



![ToolTipText](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572803088539.png







The STATS menu item brings the User to Stats Page



```kotlin
if (intent.hasExtra("email")) {   
toast("extra extra Email")    
userEmail = intent.getStringExtra("email")}

hillforts = app.hillforts.findAll().toMutableList()
if(hillforts != null) {    
   numberOfHillforts = hillforts.size
   numberVisited  = hillforts.filter {it.visit_yn}.size  
   //  hillfortsNotVisited = hillforts.filter {it.visit_yn}.toMutableList()}
   
   textView1.text = "Hi ${userEmail}"textView2.text = "This is the story so far. I ${userEmail} , have ${numberOfHillforts} in my collection. I have visited ${numberVisited} so far."
```





The Stats page receives  the logged in person email through the intents extra.

The list of all hillforts in the collection, is received through the mainApp.

app.hillforts.findAll() method, which returns a list. 

To get the number of sit visited, a lamda function is used, and each hillforts  "visited_yn" boolean property is tested, and only those marked as true are added to a new collection, and the size of that collection, is used to populate the variable "numberVisited"



![stats](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572807346205.png







there is a bug here when the button is clicked, rather than showing the list of not visited sites, it shows all sites.

![HillfortAdd](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572811520268.png







When the plus icon is selected, it brings the user into the Add Hillfort Screen.

Where you enter the title, and description.

The Add Image Button brings you an option to select an existing image from  the gallery or create a new photo.

![SelectAction](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572811951180.png





![Gallery](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572812279634.png









When Camera is chosen, permissions have to be given from user.

![Permission1](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572814674106.png



![Permission2](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572814728702.png







![Camera](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572814876218.png









the photo is taken and saved to Gallery. To add the photo to the App, you need to go into the Galley and attach from there.

![ToggleButton](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572815472462.png







The ToggleButton, is used to denote whether a site has been visited or not, once clicked the text changes.

Press to select date, is used to select the date that the site was visited on.



![Date-Picker](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572815624160.png





![date-picker](/../1572815624160-1572893641405.png)





![DeleteImage1](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572817827324.png







![DeleteImage2](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572817933151.png













To delete a Hillfort, it needs to be in edit mode, you you need to select the Hillfort.



![DeleteImage3](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572817997662.png







in this example I have deleted test3 hillfort.





##### from MapActivity

```kotlin
// taking in the name of the hillfort, to be used a Marker Title        
if (intent.hasExtra("name")) { markerName = intent.getStringExtra("name") }
```

```kotlin
override fun onMapReady(googleMap: GoogleMap) { 
mMap = googleMap    
// Add a marker in locMarker and move the camera, and set its title to the hillfort name
//
val locMarker = LatLng(mLatitudeTextView.toDouble(),mLongitudeTextView.toDouble())    mMap.addMarker(MarkerOptions().position(locMarker).title("${markerName}"))    mMap.moveCamera(CameraUpdateFactory.newLatLng(locMarker))}

```



![LocationMarker](https://github.com/murphy-mj/Hillfort/blob/master/Images/1572801506949.png



