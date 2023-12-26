package com.example.shoppinglistapp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

//to add items we are going to use data class
data class ShoppingListItem(var name: String, var count: String, var id : Int, var isEditing: Boolean = false){

}
@OptIn(ExperimentalMaterial3Api::class)//this was added to suppress the warning, we getting when we were adding the alert box
//as the alert box is still in experimental phase
@Composable
fun ShoppingListApp(){
    var sItems by remember{ mutableStateOf(listOf<ShoppingListItem>()) }//it is the list of items, items are the objects of shopping list item class
    //we will use alert box to add items to the list
    var showDialog by remember{ mutableStateOf(false) }
    //if showdialog is true then we will show the alert box, if false then we will not show the alert box
    var itemname by remember{ mutableStateOf("") }
    var itemcount by remember{ mutableStateOf("") }
    //we are setting up a varible sItem which is a list of shopping list item
    //using remember we are making sure that the list is not recreated every time the composable is recomposed
    //and mutable state of is used to change the value of the list and update the ui accordingly
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            /*//lambda expressions are used to reduce the code, it is a function that has no name
            //val doubleNummber: (Int) -> Int = { it * 2 }
            //we are creating a lambda expression and storing its value in val doubleNumber
            //here int in the round bracket is the parameter and it is the body of the function
            //int after the arrow is the return type of the function, and in curly braces we write our logic
           // Text(text = doubleNummber(5).toString()) this was just the demo for lambda expression*/
            Text(text = "Add Item")
        }
//to achive our list we are going to use lazyColumn, it is a column that only composes and lays out the currently visible items
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            //item and items are different dumb ass , items were able to use "it" but not item
           items(sItems){
               item ->  
               if(item.isEditing){
                   ShoppingItemEditor(item = item, onItemEdit = {
                       editedName, editedQuantity ->
                       sItems = sItems.map{it.copy(isEditing = false)}
                       val editedItem = sItems.find { it.id == item.id }//this code is to find out which item is being edited
                       editedItem ?.let {
                           //The safe call operator (?) ensures that the subsequent code block is executed only if editedItem is not null.
                           it.name = editedName
                           it.count = editedQuantity.toString()
                       }
                   })
               }else{
                   ShoppingListItem(item = item ,
                       onEditClick = { sItems = sItems.map{it.copy(isEditing = it.id == item.id)}},
                       //if itemid and the id of the item we are editing is same then we will set the is editing to true or else false
                       onDeleteClick = {
                           sItems = sItems -item
                       } )
               }
           }
        }
    }
    if(showDialog){
        //we will show the alert box here
        AlertDialog(onDismissRequest = {showDialog = false },
            //confirm button is used to add the item to the list
            confirmButton = {
                Button(onClick = {
                    if(itemname.isNotEmpty() && itemcount.isNotEmpty()){
                        //when we click the add button new object of shopping list item is created
                        // and is stored in the sitems list, following is the constructor of the shopping list item
                        val newItem = ShoppingListItem(itemname, itemcount, sItems.size+1)
                        //we are adding the item to the list
                        sItems = sItems + newItem
                        showDialog = false
                        itemname = ""
                        itemcount = "1"
                    }

                }) {
                    Text(text = "Add")
                }
            },
            // dissmiss button is used to dismiss the alert box, without adding anything to the list
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
            },
            title = {
                Text(text = "Add to the list")
            },
            text = {
                Column {
                    OutlinedTextField(
                        //the out line text feild is for the name of the item,
                        value = itemname,
                        onValueChange = { itemname = it },//it the changed value
                        //it is a kotlin lambda function, it is used to change the value of the itemname, it is a string
                        label = { Text(text = "Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = itemcount,
                        onValueChange = { itemcount = it },
                        label = { Text(text = "Count") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                }
            })


    }

}
@Composable
fun ShoppingItemEditor(item: ShoppingListItem, onItemEdit: (String, Int) -> Unit){
    var newName by remember{ mutableStateOf(item.name) }
    var newCount by remember{ mutableStateOf(item.count)}
    var isEditing by remember{ mutableStateOf(item.isEditing)}

    Row(modifier =
    Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Column {
            BasicTextField(value = newName,
                onValueChange = { newName = it },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp))
            BasicTextField(value = newCount,
                onValueChange = { newCount = it },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp))
        }
        Button(onClick = {
            isEditing = false
            onItemEdit(newName, newCount.toIntOrNull() ?: 1)//if the new count is valid int then it will be used else 1 will be used
        }) {
            Text(text = "Save")
        }
    }

}
@Composable
fun ShoppingListItem(
    item: ShoppingListItem,
    onEditClick: () -> Unit,
    //unit is a type in kotlin, it is similar to void in java it signifies no output
    onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().border(border = BorderStroke(2.dp, Color.Black), shape = RoundedCornerShape(8.dp)).padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty : ${item.count}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                //Icon button is used to add icons to the button, we need image vector which will add an icon
                //there are some default icons in the icon class we are using default to use them
            }
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
        }
    }


