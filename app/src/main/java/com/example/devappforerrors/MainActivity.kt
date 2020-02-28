package com.example.devappforerrors

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.devappforerrors.Extras.Constants
import com.example.devappforerrors.model.UserEmailAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.iid.FirebaseInstanceId

class MainActivity : AppCompatActivity() {

    lateinit var errorRecyclerView: RecyclerView
    lateinit var firebaseFireStore: FirebaseFirestore
    lateinit var collectionRegistration: ListenerRegistration
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeViews()
        val list = mutableListOf<String>()

        val emailAdapter = UserEmailAdapter(this, list)
        errorRecyclerView.adapter = emailAdapter
        errorRecyclerView.layoutManager = GridLayoutManager(this, 1)

        firebaseFireStore = FirebaseFirestore.getInstance()
        mAuth = FirebaseAuth.getInstance()

        mAuth.signInWithEmailAndPassword(Constants.DEV_EMAIL, Constants.DEV_PASS)
            .addOnCompleteListener { authResultTask ->
                if (authResultTask.isSuccessful) {
                    FirebaseInstanceId.getInstance()
                        .instanceId.addOnCompleteListener { instanceIdResult ->

                        val tokenMap = hashMapOf<String, Any>()
                        tokenMap[Constants.TOKEN_FIELD_NAME] = instanceIdResult.result!!.token

                        firebaseFireStore.collection(Constants.DATABASE_NAME).document(Constants.DEV_EMAIL)
                            .update(tokenMap)
                            .addOnSuccessListener {
                                Toast.makeText(
                                    this, "User signed in"
                                    , Toast.LENGTH_LONG
                                ).show()
                            }
                    }
                }
            }
            .addOnFailureListener {
                Log.d("FirebaseError", it.message!!)
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

        collectionRegistration =
            firebaseFireStore.collection(Constants.DATABASE_NAME)
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
