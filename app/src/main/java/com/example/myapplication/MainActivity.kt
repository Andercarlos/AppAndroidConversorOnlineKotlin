package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.text_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)

        buttonConverter.setOnClickListener {
            converter()
        }

    }
    private fun converter() {
        val selectedCurrency = findViewById<RadioGroup>(R.id.radioGroup)
        val checked = selectedCurrency.checkedRadioButtonId

       val currency = when (checked) {
            R.id.radio_dolar -> "USD"
            R.id.radio_euro -> "EUR"
            else -> "CLP"
        }
        val editField = findViewById<EditText>(R.id.edit_field)
        val value = editField.text.toString()

        if (value.isEmpty())
            return
            result.text = value
            result.visibility = View.VISIBLE


        Thread{
            val url = URL("https://free.currconv.com/api/v7/convert?q=${currency}_BRL&compact=ultra&apiKey=9bd189fcc8d5ba9c5509")
            val conn = url.openConnection() as HttpsURLConnection

            try {
                //Este é um processo paralelo,ou seja, por trás da interface.
                val data = conn.inputStream.bufferedReader().readText()
                val objjason = JSONObject(data)

                //Faz reiderizar na UI, caso contrário, dará erro.
                runOnUiThread {
                    val res = objjason.getDouble("${currency}_BRL")
                    result.text = "R$ ${"%.2f".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }


            } finally {
                conn.disconnect()
            }
        }.start()

    }
}