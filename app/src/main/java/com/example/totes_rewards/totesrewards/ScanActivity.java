package com.example.totes_rewards.totesrewards;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class ScanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        Bitmap myQRCode = null;
        try {
            myQRCode = BitmapFactory.decodeStream(
                    getAssets().open("qr_code.jpg")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        BarcodeDetector barcodeDetector =
                new BarcodeDetector.Builder(this)
                        .setBarcodeFormats(Barcode.QR_CODE)
                        .build();

        Frame myFrame = new Frame.Builder()
                .setBitmap(myQRCode)
                .build();

        SparseArray<Barcode> barcodes = barcodeDetector.detect(myFrame);

        // Check if at least one barcode was detected
        if(barcodes.size() != 0) {

            // Print the QR code's message
            Log.d("My QR Code's Data",
                    barcodes.valueAt(0).displayValue
            );
        }
    }
}
