function nameFilterTable() {
    const input = document.getElementById("searchNameInput").value.toLowerCase();
    const rows = document.querySelectorAll("table tbody tr");

    rows.forEach(row => {
        const nome = row.cells[0].textContent.toLowerCase();

        if (nome.includes(input)) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}

function codeFilterTable() {
    const input = document.getElementById("searchCodeInput").value.toLowerCase();
    const rows = document.querySelectorAll("table tbody tr");

    rows.forEach(row => {
        const code = row.cells[1].textContent.toLowerCase();

        if (code.includes(input)) {
            row.style.display = "";
        } else {
            row.style.display = "none";
        }
    });
}