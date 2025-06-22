const authKey = document.getElementById("authKey").getAttribute("value");
const allTicketAuthKey = document
  .getElementById("allTicketAuthKey")
  .getAttribute("value");

const url = `http://localhost:8080/API/get_all_tickets/${encodeURIComponent(authKey)}/${encodeURIComponent(allTicketAuthKey)}`;

let tickets = [];
let ticketChart = document.getElementById("openedTickets");
let chartInstance;

class Ticket {
  constructor(id, dataCreazione, dataChiusura, status) {
    this.id = id;
    this.dataCreazione = dataCreazione;
    this.dataChiusura = dataChiusura;
    this.status = status;
  }
}

function getDaysInMonth(month, year) {
  const date = new Date(year, month, 1);
  const days = [];
  while (date.getMonth() === month) {
    days.push(new Date(date));
    date.setDate(date.getDate() + 1);
  }
  return days;
}

function formatDateToYYYYMMDD(date) {
  return date.toISOString().split("T")[0];
}

function generaGrafico(mese, anno) {
  const days = getDaysInMonth(mese, anno);
  const openCounts = new Array(days.length).fill(0);
  const inProgressCounts = new Array(days.length).fill(0);
  const closedCounts = new Array(days.length).fill(0);

  for (let i = 0; i < days.length; i++) {
    const todayFormatted = formatDateToYYYYMMDD(days[i]);

    openCounts[i] = tickets.filter(
      (ticket) =>
        ticket.dataCreazione === todayFormatted &&
        ticket.status === "APERTO"
    ).length;

    inProgressCounts[i] = tickets.filter(
      (ticket) =>
        ticket.dataCreazione === todayFormatted &&
        ticket.status === "IN_CORSO"
    ).length;

    closedCounts[i] = tickets.filter(
      (ticket) =>
        ticket.dataChiusura === todayFormatted &&
        ticket.status === "CHIUSO"
    ).length;
  }

  const chartData = {
    labels: days.map((d) =>
      d.toLocaleDateString("it-IT", { day: "2-digit", month: "2-digit" })
    ),
    datasets: [
      {
        label: "Ticket Aperti",
        data: openCounts,
        backgroundColor: "rgba(87, 75, 192, 0.7)",
        borderColor: "rgba(87, 75, 192, 1)",
        borderWidth: 1,
      },
      {
        label: "Ticket In Corso",
        data: inProgressCounts,
        backgroundColor: "rgba(0, 255, 13, 0.7)",
        borderColor: "rgba(0, 200, 10, 1)",
        borderWidth: 1,
      },
      {
        label: "Ticket Chiusi",
        data: closedCounts,
        backgroundColor: "rgba(255, 0, 0, 0.7)",
        borderColor: "rgba(200, 0, 0, 1)",
        borderWidth: 1,
      },
    ],
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    interaction: {
      mode: "index",
      intersect: false,
    },
    plugins: {
      title: {
        display: true,
        text: "Andamento Ticket per Giorno",
        font: { size: 18, weight: "bold" },
      },
      tooltip: {
        callbacks: {
          label: function (context) {
            return `${context.dataset.label}: ${context.parsed.y}`;
          },
        },
      },
      legend: {
        position: "bottom",
        labels: {
          color: "#000",
          font: { size: 14 },
        },
      },
    },
    scales: {
      x: {
        title: {
          display: true,
          text: "Giorni del Mese",
          font: { size: 14, weight: "bold" },
        },
        ticks: {
          maxRotation: 45,
          minRotation: 45,
        },
        grid: { display: false },
      },
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: "Numero di Ticket",
          font: { size: 14, weight: "bold" },
        },
        ticks: { precision: 0 },
      },
    },
  };


  if (chartInstance) chartInstance.destroy();

  chartInstance = new Chart(ticketChart, {
    type: "bar",
    data: chartData,
    options: chartOptions,
  });
}


fetch(url)
  .then((response) => response.json())
  .then((data) => {
    data.forEach((item) => {
      tickets.push(new Ticket(item.id, item.dataCreazione, item.dataChiusura, item.statusName));
    });

    const today = new Date();
    generaGrafico(today.getMonth(), today.getFullYear());

    document.getElementById("filtraBtn").addEventListener("click", () => {
      const meseSelezionato = parseInt(document.getElementById("meseSelect").value);
      const anno = today.getFullYear();
      generaGrafico(meseSelezionato, anno);
    });
  })
  .catch((error) => {
    console.error("Errore nella fetch:", error);
  });
