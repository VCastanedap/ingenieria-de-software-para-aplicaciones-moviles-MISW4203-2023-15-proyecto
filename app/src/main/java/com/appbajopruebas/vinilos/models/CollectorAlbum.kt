import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.appbajopruebas.vinilos.models.Collector

@Entity(
    tableName = "collector_albums_table",
    foreignKeys = [
        ForeignKey(
            entity = Collector::class,
            parentColumns = ["id"],
            childColumns = ["collectorId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class CollectorAlbum(
    @PrimaryKey val id: Int,
    val price: Double,
    val status: String,
    val collectorId: Int // ID del coleccionista al que pertenece este álbum
)
