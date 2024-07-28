package tracker.http.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import tracker.model.Subtask;
import tracker.service.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskAdapter extends TypeAdapter<Subtask> {
    private final TypeAdapter<Duration> durationAdapter = new DurationTypeAdapter();
    private final TypeAdapter<LocalDateTime> localDateTimeAdapter = new LocalDateTimeTypeAdapter();
    private final TaskManager taskManager;

    public SubtaskAdapter(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void write(final JsonWriter jsonWriter, final Subtask subtask) throws IOException {

        if (subtask != null) {
            jsonWriter.beginObject()
                    .name("id").value(subtask.getId())
                    .name("status").value(subtask.getStatus().toString())
                    .name("name").value(subtask.getName())
                    .name("description").value(subtask.getDescription())
                    .name("epicId").value(subtask.getEpic().getId())
                    .name("duration");
            durationAdapter.write(jsonWriter, subtask.getDuration());
            jsonWriter.name("startTime");
            localDateTimeAdapter.write(jsonWriter, subtask.getStartTime());
            jsonWriter.name("type").value(subtask.getType().toString())
                    .endObject();
        } else {
            jsonWriter.nullValue();
        }
    }

    @Override
    public Subtask read(JsonReader in) throws IOException {
        int id = 0;
        String name = null;
        String description = null;
        Duration duration = null;
        LocalDateTime startTime = null;
        int epicId = 0;

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    id = in.nextInt();
                    break;
                case "name":
                    name = in.nextString();
                    break;
                case "epicId":
                    epicId = in.nextInt();
                    break;
                case "description":
                    description = in.nextString();
                    break;
                case "duration":
                    duration = durationAdapter.read(in);
                    break;
                case "startTime":
                    startTime = localDateTimeAdapter.read(in);
                    break;
            }
        }
        in.endObject();
        return new Subtask(id, name, description, duration, startTime, taskManager.getEpic(epicId));
    }
}