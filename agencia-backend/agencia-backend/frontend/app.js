const API_URL = "http://localhost:8080/api/viajes";

document.addEventListener("DOMContentLoaded", cargarViajes);

async function cargarViajes() {
    const container = document.getElementById("viajes-container");
    container.innerHTML = "<p>Cargando viajes desde la API...</p>";

    try {
        const res = await fetch(API_URL);
        if (!res.ok) throw new Error("Error al consultar el servidor");
        const viajes = await res.json();

        container.innerHTML = "";

        if (viajes.length === 0) {
            container.innerHTML = "<p>No hay viajes registrados.</p>";
            return;
        }

        viajes.forEach(viaje => {
            const porcentaje = viaje.porcentajeAvance ? viaje.porcentajeAvance.toFixed(1) : 0;
            const kmTotales = viaje.kmTotales || 0;
            const kmRestantes = viaje.kmRestantes || 0;

            const card = document.createElement("div");
            card.className = "card";
            card.innerHTML = `
                <div style="display: flex; justify-content: space-between; align-items: start;">
                    <h3 style="margin: 0 0 0.5rem 0;">${viaje.nombre}</h3>
                    <span class="badge ${viaje.estado}">${viaje.estado}</span>
                </div>
                <p style="margin: 0.2rem 0; color: #64748b;">Destino: ${viaje.destinoDelViaje ? viaje.destinoDelViaje.nombre : 'N/A'}</p>
                <p style="margin: 0.2rem 0; color: #64748b;">Pasajeros: ${viaje.cantPasajeros}</p>
                
                <div class="progress-bar">
                    <div class="progress-fill" style="width: ${porcentaje}%"></div>
                </div>
                
                <p style="font-size: 0.9rem; margin: 0.2rem 0;">
                    <b>${viaje.avanceKmRecorridos} km</b> de ${kmTotales} km (${porcentaje}%)
                </p>
                <p style="font-size: 0.85rem; color: #64748b; margin: 0;">Faltan: ${kmRestantes} km</p>

                ${viaje.estado !== 'FINALIZADO' ? `
                    <form class="advance-form" onsubmit="registrarAvance(event, ${viaje.idViaje})">
                        <input type="number" step="0.1" min="0.1" placeholder="Km" required id="km-input-${viaje.idViaje}">
                        <button type="submit">Avanzar</button>
                    </form>
                ` : '<p style="color: #166534; font-weight: bold; margin-top: 1rem;">✔ Viaje completado</p>'}
            `;
            container.appendChild(card);
        });

    } catch (err) {
        container.innerHTML = `<p style="color: red;">Error de conexión con la API: ${err.message}</p>`;
    }
}

async function registrarAvance(event, idViaje) {
    event.preventDefault();
    const input = document.getElementById(`km-input-${idViaje}`);
    const km = parseFloat(input.value);

    try {
        const res = await fetch(`${API_URL}/${idViaje}/avanzar?km=${km}`, {
            method: "PUT"
        });

        if (!res.ok) {
            const errorData = await res.json();
            alert(`Error: ${errorData.mensaje || 'No se pudo registrar el avance'}`);
            return;
        }

        input.value = "";
        cargarViajes(); // Recargar tarjetas para actualizar la barra y estado
    } catch (err) {
        alert("Error de conexión al enviar el avance.");
    }
}