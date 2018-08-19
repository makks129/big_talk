package com.makks.bigtalk.model.remote

import com.google.firebase.firestore.FirebaseFirestore

fun FirebaseFirestore.questions() = this.collection("questions")
