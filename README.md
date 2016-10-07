# SlideMenu
Sliding menu custom view. Project for 4.0+ version
##ScreenShot
![](https://github.com/henry-blue/SlideMenu/raw/master/screenShot.gif)
##Using
First you can add gradle dependency with command :
```groovy
  repositories {
    maven {
        url 'https://dl.bintray.com/henry-blue/maven/'
    }
  }
  dependencies {
	    ......
	    compile 'com.app.slidemenu:slidemenu:0.9.3'
	}

```
To add gradle dependency you need to open build.gradle (in your app folder,not in a project folder) then copy and add the dependencies there in the dependencies block;

After you add SlideMenuLayout to layout and have to create two child layout(instance of ViewGroup) at last. 
```xml
  <slidemenu.app.com.library.SlideMenuLayout
     android:layout_width="match_parent"
     android:layout_height="70dp"
     android:minHeight="70dp" >
   
     //................
   
  </slidemenu.app.com.library.SlideMenuLayout>
```

You can get the control interface of SlideMenuLayout by the following ways:

```java
    slideMenuLayout.SetOnSlideMenuStatusListener(new SlideMenuLayout.OnSlideMenuStatusListener() {
        @Override
        public void OnStartOpenMenu() {
                
        }

        @Override
        public void OnOpenedMenu() {
               
        }

        @Override
        public void OnStartCloseMenu() {

        }

        @Override
        public void OnClosedMenu() {
                   
        }

        @Override
        public void OnSlidingMenu() {

        }
     });
```



