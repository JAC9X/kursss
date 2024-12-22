package com.example.curs.ui.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.curs.R;
import com.example.curs.models.BusinessCard;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.BusinessCardViewHolder> {

    private List<BusinessCard> businessCards;
    private Context context;

    public BusinessCardAdapter(Context context) {
        this.context = context;
    }

    public void submitList(List<BusinessCard> businessCards) {
        this.businessCards = businessCards;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusinessCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_business_card, parent, false);
        return new BusinessCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessCardViewHolder holder, int position) {
        BusinessCard businessCard = businessCards.get(position);
        holder.nameTextView.setText(businessCard.getName());
        holder.companyTextView.setText(businessCard.getCompany());
        holder.positionTextView.setText(businessCard.getPosition());
        holder.phoneTextView.setText(businessCard.getPhone());
        holder.emailTextView.setText(businessCard.getEmail());

        // Click-to-Call
        holder.phoneTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + businessCard.getPhone()));
            v.getContext().startActivity(intent);
        });

        // Email Integration
        holder.emailTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:" + businessCard.getEmail()));
            v.getContext().startActivity(intent);
        });

        // Generate vCard QR Code
        String vCard = "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:" + businessCard.getName() + "\n" +
                "FN:" + businessCard.getName() + "\n" +
                "ORG:" + businessCard.getCompany() + "\n" +
                "TITLE:" + businessCard.getPosition() + "\n" +
                "TEL;TYPE=CELL:" + businessCard.getPhone() + "\n" +
                "EMAIL:" + businessCard.getEmail() + "\n" +
                "END:VCARD";

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(vCard, BarcodeFormat.QR_CODE, 200, 200);
            Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);

            for (int x = 0; x < 200; x++) {
                for (int y = 0; y < 200; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            holder.qrCodeImageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }

        // Save Business Card as PNG
        holder.saveButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                saveBusinessCardAsPNG(holder, businessCard);
            }
        });
    }

    private void saveBusinessCardAsPNG(BusinessCardViewHolder holder, BusinessCard businessCard) {
        Bitmap bitmap = Bitmap.createBitmap(holder.itemView.getWidth(), holder.itemView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        holder.itemView.draw(canvas);

        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "BusinessCards");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, businessCard.getName() + ".png");
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(context, "Business card saved as PNG", Toast.LENGTH_SHORT).show();

            // Обновление медиа-библиотеки
            MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, businessCard.getName(), null);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Failed to save business card", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return businessCards == null ? 0 : businessCards.size();
    }

    static class BusinessCardViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView companyTextView;
        TextView positionTextView;
        TextView phoneTextView;
        TextView emailTextView;
        ImageView qrCodeImageView;
        View saveButton;

        BusinessCardViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            companyTextView = itemView.findViewById(R.id.companyTextView);
            positionTextView = itemView.findViewById(R.id.positionTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
            qrCodeImageView = itemView.findViewById(R.id.qrCodeImageView);
            saveButton = itemView.findViewById(R.id.saveButton);
        }
    }
}
