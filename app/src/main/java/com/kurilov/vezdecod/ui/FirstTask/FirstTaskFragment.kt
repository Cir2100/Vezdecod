package com.kurilov.vezdecod.ui.FirstTask

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kurilov.vezdecod.databinding.FragmentFirstTaskBinding
import com.kurilov.vezdecod.MyApp
import com.kurilov.vezdecod.R

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class FirstTaskFragment : Fragment() {

    private var _binding: FragmentFirstTaskBinding? = null
    private lateinit var viewModel: FirstTaskFragmentViewModel
    private lateinit var adapter : MyFriendsListAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstTaskBinding.inflate(inflater, container, false)

        createFriendList(inflater, container)
        viewModel = ViewModelProvider(this).get(FirstTaskFragmentViewModel::class.java)
        uiSubscribe()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun uiSubscribe() {

        viewModel.userInfo.observe(viewLifecycleOwner) { value ->
            value?.let {
                binding.textviewName.text = value.firstName
                binding.textviewCountFriend.text = value.counters?.friends.toString()
                viewModel.loadUserPhoto(value)
                viewModel.loadFriendsList(value)
            }
        }

        viewModel.userPhoto.observe(viewLifecycleOwner) { value ->
            value?.let {
                binding.imageviewUserPhoto.background = value
            }
        }

        viewModel.toast.observe(viewLifecycleOwner) { text ->
            text?.let {
                Toast.makeText(MyApp.appContext, text,
                    Toast.LENGTH_SHORT).show()
                viewModel.onToastShown()
            }
        }

        viewModel.friendList.observe(viewLifecycleOwner) { value ->
            value?.let {
                Log.e(this.javaClass.simpleName, "Printing conversations")
                adapter.updateItems(value)
            }
        }
    }

    fun createFriendList(inflater: LayoutInflater, container: ViewGroup?) {
        adapter = MyFriendsListAdapter()
        binding.friendList.setHasFixedSize(true)
        binding.friendList.adapter = adapter
        binding.friendList.layoutManager = LinearLayoutManager(inflater.inflate(R.layout.fragment_friend_list, container, false).context)
    }
}