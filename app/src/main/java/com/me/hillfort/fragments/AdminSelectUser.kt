
package com.me.hillfort.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ReportFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.me.hillfort.R
import com.me.hillfort.main.MainApp
import com.me.hillfort.fragments.PlacemarkAdapter
import com.me.hillfort.fragments.PlacemarkListener
import com.me.hillfort.models.HillfortModel
import com.me.hillfort.fragments.EditFragment
import com.me.hillfort.models.UserModel
import com.me.hillfort.utils.SwipeToDeleteCallback
import com.me.hillfort.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.fragment_basicreport.view.*
import kotlinx.android.synthetic.main.fragment_basicreport.view.swiperefresh
import kotlinx.android.synthetic.main.fragment_report.view.*
import kotlinx.android.synthetic.main.fragment_user.view.*
//import com.me.hillfort.utils.createLoader
//import com.me.hillfort.utils.hideLoader
//import com.me.hillfort.utils.showLoader
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class AdminSelectUser : Fragment() ,AnkoLogger, UserListener {

    lateinit var root: View
    lateinit var app: MainApp
    lateinit var loader : AlertDialog
    lateinit var userSelected :UserModel
    lateinit var userList : ArrayList<UserModel>
  //  lateinit var userList : ArrayList<String>
  //  lateinit var placemarksListFrag : ArrayList<HillfortModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        info("in Admin Select User on Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        activity?.title = "${app.auth.currentUser!!.uid}"
        root =  inflater.inflate(R.layout.fragment_user, container, false)
        root.recyclerViewU.setLayoutManager(LinearLayoutManager(activity))
        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = root.recyclerViewU.adapter as PlacemarkAdapter
                adapter.removeAt(viewHolder.adapterPosition)
              //  deleteFavUserPlacemark(app.auth.currentUser!!.uid,
              //      (viewHolder.itemView.tag as UserModel).uid)
            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(root.recyclerViewU)

        val swipeEditHandler = object : SwipeToEditCallback(activity!!) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onUserClick(viewHolder.itemView.tag as UserModel)
            }
        }

        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(root.recyclerViewU)


     return root
    }

    companion object {
        @JvmStatic
        var userSelected :String = ""
        fun newInstance() =
            AdminSelectUser().apply {
                arguments = Bundle().apply { }
            }
    }

    fun setSwipeRefresh() {
        info("In set swipeRefresh")
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
              //  placemarksListFrag = app.pObj.findAll2()
                getAllUsers(app.auth.currentUser!!.uid)
              //  fetchUsers2 {  }
            }
        })
    }





    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    override fun onUserClick(user: UserModel) {

        val args = Bundle()
     //   args.putParcelable("selectedUser",user )
        args.putString("selectedUser",user.Uuid )
        var frag = AdminSelectHillforts.newInstance()
        frag.arguments=args
        activity!!.supportFragmentManager.beginTransaction()
            .replace(R.id.homeFrame,frag )
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        info("On Resume : ${app.auth!!.uid}")
        getAllUsers(app.auth.currentUser!!.uid)
      //  fetchUsers2{}
    }





    fun getAllUsers(userId: String?) {
        Toast.makeText(activity, "Get All Users ", Toast.LENGTH_LONG).show()
        //    loader = createLoader(activity!!)
        //    showLoader(loader, "Downloading Donations from Firebase")
        userList = ArrayList<UserModel>()
       // userList = ArrayList<String>()
       // userList.clear()
        // app.database.child("users").child(userId!!).child("placemarks")
        app.pObj.db.child("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    info("Firebase Users error : ${error.message}")
                }
                override fun onDataChange(snapshot: DataSnapshot) {
                    //      hideLoader(loader)
                    val children = snapshot!!.children
                    children.forEach {
                        val user = it.
                           getValue<UserModel>(UserModel::class.java)
                      //  val userId = it.value as String
                        info("Users user uuid : ${user!!.Uuid}")
                        userList.add(user!!)
                        root.recyclerViewU.adapter =
                            UserAdapter(userList, this@AdminSelectUser)
                        root.recyclerViewU.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()
                        //    app.database.child("users").child(userId).child("placemarks")
                        app.pObj.db.child("users")
                            .removeEventListener(this)
                    }
                }
            })
    }



    fun fetchUsers2(usersReady: () -> Unit) {
        info("In FetchUsers 2")
       var userList = ArrayList<UserModel>()
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(userList) { it.getValue<UserModel>(UserModel::class.java) }
                usersReady()
            }
        }
        var db: DatabaseReference  = FirebaseDatabase.getInstance().reference
        //app.pObj.db.child("users").addListenerForSingleValueEvent(valueEventListener)
        db.child("users").addValueEventListener(valueEventListener)

    }




}
