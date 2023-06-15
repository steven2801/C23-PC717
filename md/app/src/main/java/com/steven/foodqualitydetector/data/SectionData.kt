package com.steven.foodqualitydetector.data

import com.steven.foodqualitydetector.R

object SectionData {
    val sections = listOf(
        Section(
            resource = R.drawable.onboarding_illustration,
            title = "Selamat datang di Food Quality Detector!",
            description = "Food Quality Detector adalah sebuah aplikasi untuk mendeteksi apakah suatu makanan masih layak dikonsumsi."
        ),
        Section(
            resource = R.drawable.scan,
            title = "Scan",
            description = "Ambil gambar makanan yang Anda inginkan."
        ),
        Section(
            resource = R.drawable.details,
            title = "Hasil",
            description = "Hasil scan tersebut akan muncul dalam beberapa saat."
        ),
        Section(
            resource = R.drawable.simpan,
            title = "Simpan",
            description = "Riwayat makanan akan tersimpan secara otomatis."
        )
    )
}