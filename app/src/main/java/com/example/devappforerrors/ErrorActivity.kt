package com.example.devappforerrors

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devappforerrors.model.ErrorAdapter
import com.example.devappforerrors.model.ErrorListModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class ErrorActivity : AppCompatActivity() {

    lateinit var errorRecyclerView: RecyclerView
    lateinit var firebaseFireStore: FirebaseFirestore
    lateinit var collectionRegistration: ListenerRegistration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_error)
        val email = intent.getStringExtra("email")
        supportActionBar?.title = email
        initializeViews()
        val list= mutableListOf<ErrorListModel>()

        firebaseFireStore = FirebaseFirestore.getInstance()
        errorRecyclerView.adapter = ErrorAdapter(this, list)
        errorRecyclerView.layoutManager = GridLayoutManager(this, 1)

        collectionRegistration = firebaseFireStore.collection("Users").document(email!!).collection("Notifications")
            .addSnapshotListener { querySnapshot, e ->
                if (e != null) {
                    Log.d("FirebaseError", e.message!!)
                } else {
                    for (doc in querySnapshot!!.documentChanges) {
                        val notifications = doc.document.toObject(ErrorListModel::class.java)
                        list.add(notifications)
                    }
                    errorRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
    }

    private fun initializeViews() {
        errorRecyclerView = findViewById(R.id.error_list_rv)
    }

    override fun onPause() {
        super.onPause()
        collectionRegistration.remove()
    }
}
