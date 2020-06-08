package com.example.friendzone.manager

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.friendzone.UploadManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log

class ApiManager(databaseRef: DatabaseReference) {
    var dbRef = databaseRef
    private val membersDatabaseRef = dbRef.child("groups")
    var groupMembers = mutableListOf<String>()

    fun asyncFetch(groupMap: HashMap<String, List<String>>, group: String, onSuccess: () -> Unit, onError: (() -> Unit)? = null) {

        val eachGroup = membersDatabaseRef.child(group).child("members")

        eachGroup.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupMembers = mutableListOf()
                for (ds in dataSnapshot.children) {
                    groupMembers.add(ds.value.toString())
                }
                groupMap[group] = groupMembers
//                Log.i("JY", group)
//                Log.i("JY", groupMembers.toString())
                onSuccess.invoke()
            }

            override fun onCancelled(p0: DatabaseError) {
                onError?.invoke()
            }

        })
    }


    fun fetchPost(group: String, onSuccess: (MutableList<UploadManager>) -> Unit, onError: (() -> Unit)? = null) {

        val groupContent = membersDatabaseRef.child(group)
        groupContent.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var postList: MutableList<UploadManager> = mutableListOf()

                for (ds in dataSnapshot.children) {
                    if (ds.key != "members") {
                        val post = ds.getValue(UploadManager::class.java)
                        postList.add(post!!)
                    }
                }
                onSuccess.invoke(postList)
            }

            override fun onCancelled(p0: DatabaseError) {
                onError?.invoke()
            }

        })
    }

    fun addUser(user: String, group: String, onSuccess: (String) -> Unit, onError: (() -> Unit)? = null, context: Context, createGroup: Boolean) {
        var findUser = false
        var updated = false
        var groupMemberUpdated = false
        var userName: String? = null


        val userSearch = dbRef.child("user")

        userSearch.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (ds in dataSnapshot.children) {
                    val userEmail = ds.child("email").getValue(String::class.java)

                    if (user == userEmail) {
                        Log.i("USER", "find")
                        findUser = true
                        userName = ds.child("userName").value.toString()

                        var userGroup = ds.child("groupList").value



                        if (userGroup != null) {
                            val groupList = userGroup as MutableList<String>
                            groupList.add(group)
//                            userGroup = userGroup as MutableList<String>
//                            userGroup = userGroup.add(group)

                            if (!updated) {
                                userSearch.child(ds.child("uid").value as String)
                                    .child("groupList")
                                    .setValue(groupList)
                                updated = true
                                Toast.makeText(
                                    context,
                                    "added $user to $group",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        } else {
                            val groupList = arrayListOf<String>()
                            groupList.add(group)

                            if (!updated) {
                                userSearch.child(ds.child("uid").value as String)
                                    .child("groupList")
                                    .setValue(groupList)
                                updated = true
                                Toast.makeText(
                                    context,
                                    "added $user to $group",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        if (!createGroup) {
                            val groupMembers = dbRef.child("groups").child(group).child("members")
                            groupMembers.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (!groupMemberUpdated) {
                                        val member = dataSnapshot.value as MutableList<String>
                                        member.add(userName!!)
                                        Log.i("XXX", member.toString())
                                        groupMembers.setValue(member)

                                        groupMemberUpdated = true
                                    }
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                    onError?.invoke()
                                }
                            })
                        } else {
                            val groupMembers = dbRef.child("groups").child(group).child("members")
                            groupMembers.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(p0: DataSnapshot) {
                                    if (!groupMemberUpdated) {
                                        val memberList = arrayListOf<String>()
                                        memberList.add(userName!!)

                                        groupMembers.setValue(memberList)

                                        groupMemberUpdated = true
                                    }
                                }

                                override fun onCancelled(p0: DatabaseError) {
                                }
                            })
                        }

                    }
                }
//                if (findUser) {
//                    onSuccess.invoke(userName!!)
//                } else {
//                    Log.i("USER", user)
//                    onError?.invoke()
//                }
            }


            override fun onCancelled(p0: DatabaseError) {
                onError?.invoke()
            }
        })

    }


}