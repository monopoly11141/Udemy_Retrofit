package com.example.udemy_retrofit

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import com.example.udemy_retrofit.databinding.ActivityMainBinding
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var retrofitService : AlbumService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        retrofitService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        //getRequestWithQueryParameters()
        uploadAlbum()
    }

    private fun getRequestWithQueryParameters() {

        val responseLiveData: LiveData<Response<Albums>> = liveData {
            val response = retrofitService.getSortedAlbums(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if (albumsList != null) {
                while (albumsList.hasNext()) {
                    val albumsItem = albumsList.next()
                    val result = " Album title : ${albumsItem.title}\n" +
                            " Album id : ${albumsItem.id}\n" +
                            " User id : ${albumsItem.userId}\n\n\n"
                    binding.tvAlbum.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameters() {
        val pathResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.getAlbum(3)
            emit(response)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
        })
    }

    private fun uploadAlbum() {
        val album = AlbumsItem(0, "New Album", 12)

        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retrofitService.uploadAlbum(album)
            emit(response)
        }

        postResponse.observe(this, Observer {
            val receivedAlbumsItem = it.body()!!
            val result = " Album title : ${receivedAlbumsItem.title}\n" +
                    " Album id : ${receivedAlbumsItem.id}\n" +
                    " User id : ${receivedAlbumsItem.userId}\n\n\n"
            binding.tvAlbum.text = result
        })
    }

}