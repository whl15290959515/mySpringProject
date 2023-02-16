package com.example.myspringdemo.entity

class DishDto :Dish() {
    var flavors: List<DishFlavor> = ArrayList()

    var categoryName:String?=null

    var copies:Int=0

}

