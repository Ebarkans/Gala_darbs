const plantData = [
    { number: 1, name: "Monstera", summertime: "Skapis", wintertime: "Istaba", watering: "Reizi nedēļā" },
    { number: 2, name: "Kaktuss", summertime: "Ārā", wintertime: "Istaba", watering: "Reizi mēnesī" },
    { number: 3, name: "Monstera", summertime: "Palodze", wintertime: "Istaba", watering: "Reizi nedēļā" },
    { number: 4, name: "Kaktuss", summertime: "Ārā", wintertime: "Istaba", watering: "Reizi mēnesī" },
    { number: 5, name: "Alveja", summertime: "Palodze", wintertime: "Palodze", watering: "Reizi divās nedēļās" },
    { number: 6, name: "Orhideja", summertime: "Palodze", wintertime: "Virtuve", watering: "Reizi nedēļā, mērcējot" },
    { number: 7, name: "Fikuss", summertime: "Jumts", wintertime: "Gaiša istaba", watering: "Kad augsne izžuvusi" }
    ];
  
  const tableBody = document.querySelector("#plantTable tbody");
  
  plantData.forEach(plant => {
    const row = tableBody.insertRow();
    row.insertCell().textContent = plant.number;
    row.insertCell().textContent = plant.name;
    row.insertCell().textContent = plant.summertime;
    row.insertCell().textContent = plant.wintertime;
    row.insertCell().textContent = plant.watering;
  
    const actionCell = row.insertCell();
    const editButton = document.createElement("button");
    editButton.textContent = "Rediģēt";
    editButton.addEventListener("click", () => {
      // Šeit pievienojiet loģiku rediģēšanas formas atvēršanai
      alert(`Rediģēt augu ${plant.name}`);
    });
    actionCell.appendChild(editButton);
  });
  