# ScalableDottedBar
[![](https://jitpack.io/v/ZeyuKeithFu/ScalableDottedBar.svg)](https://jitpack.io/#ZeyuKeithFu/ScalableDottedBar)

An Instagram-like dotted progress bar for multi-image flipping.

<img src="https://github.com/ZeyuKeithFu/ScalableDottedBar/blob/demo/demo.gif" height="600"/>

## Integration
Add JitPack to your root `build.gradle`
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Add the dependency
```
dependencies {
	implementation 'com.github.ZeyuKeithFu:ScalableDottedBar:1.0'
}
```

## Usage
```
<com.zeyufu.scalabledottedbar.ScalableDottedBar
    android:id="@+id/barDotted"
    android:layout_width="match_parent"
    android:layout_height="120dp"
    android:spacing="12dp"
    android:radius="8dp"
    app:numDots="7"
    app:maxInitial="5"
    app:scaleLevel="3"
    app:scaleMultiplier="0.6"
    app:colorUnselected="@android:color/darker_gray"
    app:colorSelected="@android:color/holo_blue_dark" />
```
Attributes can also be set programmatically.

## Attributes
| Name | Format | Description |
|:---|:---|:---|
| android:spacing | dimension | Distance between the centers of two adjacent dots
| android:radius | dimention | Radius of a dot
| numDots | int | Total number of dots need to be represented
| maxInitial | int | Maximum initial dots appears in the bar
| scaleLevel | int | Number of different dot scales
| scaleMultiplier | float | The zoom level between adjacent dots
| colorUnselected | int | The color of unselected dot
| colorSelected | int | The color of current selected dot

## License
[MIT License](https://github.com/ZeyuKeithFu/ScalableDottedBar/blob/master/LICENSE)