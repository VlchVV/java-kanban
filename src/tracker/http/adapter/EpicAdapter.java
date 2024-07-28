package tracker.http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.model.Epic;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class EpicAdapter extends TypeAdapter<Epic> {
    private final TypeAdapter<Duration> durationAdapter = new DurationTypeAdapter();
    private final TypeAdapter<LocalDateTime> localDateTimeAdapter = new LocalDateTimeTypeAdapter();

    @Override
    public void write(final JsonWriter jsonWriter, final Epic epic) throws IOException {

        if (epic != null) {
            jsonWriter.beginObject()
                    .name("id").value(epic.getId())
                    .name("status").value(epic.getStatus().toString())
                    .name("name").value(epic.getName())
                    .name("description").value(epic.getDescription())
                    .name("duration");
            durationAdapter.write(jsonWriter, epic.getDuration());
            jsonWriter.name("startTime");
            localDateTimeAdapter.write(jsonWriter, epic.getStartTime());
            jsonWriter.name("type").value(epic.getType().toString())
                    .endObject();
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public Epic read(JsonReader in) throws IOException {
        int id = 0;
        String name = null;
        String description = null;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    id = in.nextInt();
                    break;
                case "name":
                    name = in.nextString();
                    break;
                case "description":
                    description = in.nextString();
                    break;
            }
        }
        in.endObject();
        return new Epic(id, name, description);
    }
}