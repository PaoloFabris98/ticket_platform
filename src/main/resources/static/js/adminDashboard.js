const authKey = document.getElementById("authKey").getAttribute("value")
const allTicketAuthKey = document.getElementById("allTicketAuthKey").getAttribute("value")
const url = `http://localhost:8080/API/get_all_tickets/${authKey}/${allTicketAuthKey}`
console.log(authKey);
console.log(allTicketAuthKey);
console.log(url);

let tickets = [];

class ticket{
    constructor(id,dataCreazione,dataChiusura,status){
        this.id = id;
        this.dataCreazione = dataCreazione;
        this.dataChiusura = dataChiusura;
        this.status = status;
    }
}

fetch(url)
  .then(response => response.json())
  .then(data => {
    
    data.forEach(item => {
      const nuovoTicket = new ticket(
        item.id,
        item.dataCreazione,
        item.dataChiusura,
        item.statusName
      );
      tickets.push(nuovoTicket);
    });

    console.log("Tutti i ticket:", tickets);
  })
  .catch(error => {
    console.error("Errore nella fetch:", error);
  });