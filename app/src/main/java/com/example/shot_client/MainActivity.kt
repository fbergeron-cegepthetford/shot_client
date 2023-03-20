package com.example.shot_client

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.shot_client.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {


    private val urlCdJ = "http://10.0.2.2:3001/cdj"
    val urlFait = "http://10.0.2.2:3001/fait"
    val ACCESS_TOKEN = "967d1dc7-555b-4ee3-a98c-3210245e4a11"

    private lateinit var queue: RequestQueue

    private lateinit var ctx : Context
    private var tempBinding: ActivityMainBinding? = null
    private val binding get() = tempBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tempBinding = ActivityMainBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        ctx = this

        queue = Volley.newRequestQueue(this)

        binding.btnCitation.setOnClickListener { requeteCDJ() }
        binding.btnFait.setOnClickListener { requeteFait() }
    }

    private fun requeteCDJ(){
        val req = JsonObjectRequest(
            Request.Method.GET, urlCdJ, null,
            { response: JSONObject ->
                try {
                    binding.tvCitation.text = response.getString("citation")
                    binding.tvAuteurDate.text = getString(
                        R.string.auteur_date,
                        response.getString("auteur"),
                        response.getString("annee")
                    )
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        ) { error: VolleyError ->
            Log.d("Main", error.message!!)
        }

        queue.add(req)
    }

    private fun requeteFait(){
        val req: JsonObjectRequest = object : JsonObjectRequest(
            Method.GET, urlFait, null,
            Response.Listener { response: JSONObject ->
                try {
                    binding.tvFait.text = response.getString("description")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error: VolleyError ->
                Log.d("Main", error.message!!)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["Content-Type"] = "application/json; charset=UTF-8"
                params["shot-key-api"] = ACCESS_TOKEN
                return params
            }
        }
        queue.add(req)
    }
}