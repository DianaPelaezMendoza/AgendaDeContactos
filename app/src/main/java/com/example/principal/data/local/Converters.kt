import androidx.room.TypeConverter
import com.example.principal.data.local.dao.ContactSource

class Converters {

    @TypeConverter
    fun fromContactSource(source: ContactSource): String {
        return source.name
    }

    @TypeConverter
    fun toContactSource(value: String): ContactSource {
        return ContactSource.valueOf(value)
    }
}

