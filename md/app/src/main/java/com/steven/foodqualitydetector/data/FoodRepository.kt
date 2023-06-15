package com.steven.foodqualitydetector.data

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FoodRepository {
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val _foods: MutableList<Food> = mutableListOf()

    suspend fun getFoods(userId: String): Flow<List<Food>> = callbackFlow {
        val itemsRef: DatabaseReference = database.reference.child("users/${userId}")

        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _foods.clear()
                val foods = snapshot.children.mapNotNull { it.getValue(Food::class.java) }
                    .sortedByDescending { it.createdAt }
                _foods.addAll(foods)
                trySend(foods)
            }

            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }

        itemsRef.addValueEventListener(valueEventListener)

        awaitClose { itemsRef.removeEventListener(valueEventListener) }
    }

    fun getFoodById(id: String): Food {
        return _foods.first { it.id == id }
    }

    fun searchFoods(query: String): List<Food> {
        return _foods.filter {
            it.title.contains(query, ignoreCase = true)
        }
    }
}