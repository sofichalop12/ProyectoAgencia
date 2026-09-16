package persistence;
//LIBRERIAS IMPORTADAS DE GSON(es una libreria que serializa y deserializa, osea transforma de json a clases de java)
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import model.*;
import java.lang.reflect.Type;

public class TransporteDeserializer implements JsonDeserializer<Transporte>{ //es una interfaz JsonDeserializer
    @Override
    public Transporte deserialize(JsonElement json, Type typeOFT,JsonDeserializationContext context)
            throws JsonParseException{ // esto se hace en caso de que pasen un dato incorrecto, ej: Bicicleta
        JsonObject jsonObject = json.getAsJsonObject();//convierte pedazo de JSON en un objeto que se puede consultar
        String tipo = jsonObject.get("tipo").getAsString();
        switch (tipo){
            case "Auto":
                return context.deserialize(jsonObject,Auto.class);//lei un auto lo creo en clase auto

            case "Combi":
                return context.deserialize(jsonObject, Combi.class);

            case  "ColectivoSemiCama":
                return context.deserialize(jsonObject,ColectivoSemiCama.class);

            case "ColectivoCocheCama":
                return context.deserialize(jsonObject,ColectivoCocheCama.class);

            default:
                throw new JsonParseException("Tipo de transporte desconocido: " + tipo);
        }
    }
}
