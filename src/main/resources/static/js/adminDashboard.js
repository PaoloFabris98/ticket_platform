const authKey = document.getElementById("authKey").getAttribute("value");
const allTicketAuthKey = document
  .getElementById("allTicketAuthKey")
  .getAttribute("value");
const url = `http://localhost:8080/API/get_all_tickets/${encodeURIComponent(
  authKey
)}/${encodeURIComponent(allTicketAuthKey)}`;
console.log(authKey);
console.log(allTicketAuthKey);
console.log(url);

let tickets = [];
let ticketChart = document.getElementById("openedTickets");

const temp = new Date();
const year = temp.getFullYear();
const month = temp.getMonth();
const days = getDaysInMonth(month, year);

class ticket {
  constructor(id, dataCreazione, dataChiusura, status) {
    this.id = id;
    this.dataCreazione = dataCreazione;
    this.dataChiusura = dataChiusura;
    this.status = status;
  }
}

function getDaysInMonth(month, year) {
  var date = new Date(year, month, 1);
  var days = [];
  while (date.getMonth() === month) {
    days.push(new Date(date));
    date.setDate(date.getDate() + 1);
  }
  return days;
}

function formatDateToYYYYMMDD(date) {
  return date.toISOString().split("T")[0];
}

fetch(url)
  .then((response) => response.json())
  .then((data) => {
    data.forEach((item) => {
      const nuovoTicket = new ticket(
        item.id,
        item.dataCreazione,
        item.dataChiusura,
        item.statusName
      );
      tickets.push(nuovoTicket);
    });

    console.log("Tutti i ticket:", tickets);

    const openCounts = new Array(days.length);
    const inProgressCounts = new Array(days.length);
    const closedCounts = new Array(days.length);

    for (let i = 0; i < days.length; i++) {
      const day = days[i];

      const todayFormatted = formatDateToYYYYMMDD(day);

      openCounts[i] = tickets.filter(
        (ticket) =>
          ticket.dataCreazione === todayFormatted && ticket.status === "APERTO"
      ).length;
      
      inProgressCounts[i] = tickets.filter(
        (ticket) =>
          ticket.dataCreazione === todayFormatted && ticket.status === "IN_CORSO"
      ).length;

      closedCounts[i] = tickets.filter(
        (ticket) =>
          ticket.dataChiusura === todayFormatted && ticket.status === "CHIUSO"
      ).length;
    }
    console.log(openCounts)

    new Chart(ticketChart, {
      type: "bar",
      data: {
        labels: days.map((d) => d.getDate()),
        datasets: [
          {
            label: "Ticket Aperti",
            data: openCounts,
            backgroundColor: "rgba(87, 75, 192, 0.5)",
          },
          {
            label: "Ticket In Corso",
            data: inProgressCounts,
            backgroundColor: "rgba(0, 255, 13, 0.5)",
          },
          {
            label: "Ticket Chiusi",
            data: closedCounts,
            backgroundColor: "rgba(255, 0, 0, 0.5)",
          },
        ],
      },
      options: {
        responsive: true,
      },
    });
  })
  .catch((error) => {
    console.error("Errore nella fetch:", error);
  });
