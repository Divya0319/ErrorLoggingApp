package com.example.devappforerrors

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devappforerrors.model.UserEmailAdapter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class MainActivity : AppCompatActivity() {

    lateinit var errorRecyclerView: RecyclerView
    lateinit var firebaseFireStore: FirebaseFirestore
    lateinit var collectionRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        val list = mutableListOf<String>()

        val emailAdapter = UserEmailAdapter(this, list)
        errorRecyclerView.adapter = emailAdapter
        errorRecyclerView.layoutManager = GridLayoutManager(this, 1)

        firebaseFireStore = FirebaseFirestore.getInstance()

        collectionRegistration =
            firebaseFireStore.collection("Users")
                .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                    if (firebaseFirestoreException != null) {
                        Log.d("FirebaseError", firebaseFirestoreException.message!!)
                    } else {
                        for (email in querySnapshot!!.documentChanges) {
                            list.add(email.document.id)
                        }
                        errorRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }
    }

    private fun initializeViews() {
        errorRecyclerView = findViewById(R.id.email_list_rv)
    }

    override fun onPause() {
        super.onPause()
        collectionRegistration.remove()
    }
}
