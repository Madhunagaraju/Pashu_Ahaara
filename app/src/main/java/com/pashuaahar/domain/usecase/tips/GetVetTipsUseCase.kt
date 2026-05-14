package com.pashuaahar.domain.usecase.tips

import com.pashuaahar.domain.model.TipCategory
import com.pashuaahar.domain.model.VetTip
import javax.inject.Inject

class GetVetTipsUseCase @Inject constructor() {
    operator fun invoke(): List<VetTip> = listOf(
        VetTip(
            id = "h1",
            title = "Udder Cleaning Routine",
            content = "Before milking, wash the udder with clean water, wipe with a dry cloth, and keep the floor dry. This summary stays available offline after install.",
            category = TipCategory.HYGIENE,
            durationLabel = "2 min guide",
            videoUrl = "https://www.youtube.com/watch?v=S08R3Gv8_m4"
        ),
        VetTip(
            id = "h2",
            title = "Bucket Disinfection",
            content = "Rinse buckets after every use and disinfect daily to reduce mastitis and water-borne infection risk. Use the in-app summary even without network.",
            category = TipCategory.HYGIENE,
            durationLabel = "1 min guide",
            videoUrl = "https://www.youtube.com/watch?v=9X7qL7p2h48"
        ),
        VetTip(
            id = "f1",
            title = "Dry Feed Storage Rack",
            content = "Store maize, bran, and cakes at least 6 inches above the floor. Keep the room ventilated so the home-made balanced feed stays fungus-free.",
            category = TipCategory.FODDER_STORAGE,
            durationLabel = "2 min guide",
            videoUrl = "https://www.youtube.com/watch?v=fXp07z88e0o"
        ),
        VetTip(
            id = "f2",
            title = "Oil Cake Moisture Check",
            content = "Cottonseed cake and mustard cake should be stored in closed bins. If lumps or smell appear, dry the batch before mixing it into the ration.",
            category = TipCategory.FODDER_STORAGE,
            durationLabel = "90 sec guide",
            videoUrl = "https://www.youtube.com/watch?v=mD6uV0N8v88"
        ),
        VetTip(
            id = "d1",
            title = "Vaccination Reminder",
            content = "Follow the local veterinarian schedule for FMD and HS and note the next due date near the feeding area so every caretaker sees it.",
            category = TipCategory.DISEASE_PREVENTION,
            durationLabel = "1 min guide",
            videoUrl = "https://www.youtube.com/watch?v=4rY057uD1sc"
        ),
        VetTip(
            id = "d2",
            title = "Watch Feed Intake",
            content = "If a cow suddenly stops eating balanced feed or water for a day, isolate the animal and contact a vet early instead of waiting for yield to drop further.",
            category = TipCategory.DISEASE_PREVENTION,
            durationLabel = "90 sec guide",
            videoUrl = "https://www.youtube.com/watch?v=2Tz8l6n_v0s"
        ),
        VetTip(
            id = "w1",
            title = "Clean Water Points",
            content = "Balanced feed only works well when clean water is always available. Scrub the drinking point daily, especially during summer.",
            category = TipCategory.WATER_MANAGEMENT,
            durationLabel = "1 min guide",
            videoUrl = "https://www.youtube.com/watch?v=8q-p0_7kE2A"
        ),
        VetTip(
            id = "w2",
            title = "Hot Weather Hydration",
            content = "Offer water 4 to 5 times a day and provide shade near the drinking area. Good hydration protects milk yield and feed conversion.",
            category = TipCategory.WATER_MANAGEMENT,
            durationLabel = "2 min guide",
            videoUrl = "https://www.youtube.com/watch?v=X2B6_3rP-3M"
        )
    )
}
