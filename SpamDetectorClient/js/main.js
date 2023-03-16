// TODO: onload function should retrieve the data needed to populate the UI
  window.onload = function() {
  // Call the RESTful API to get the data
  fetch('http://localhost:8080/spamDetector-1.0/api/spam')
    .then(response => response.json())
    .then(data => {
      // Populate the table with the data
      const table = document.getElementById('data-table');
      data.forEach(item => {
        const row = table.insertRow(-1);
        const filenameCell = row.insertCell(0);
        const detectorCell = row.insertCell(1);
        const actualCell = row.insertCell(2);
        filenameCell.textContent = item.filename;
        detectorCell.textContent = item.detector;
        actualCell.textContent = item.actual;
      });

      // Set the summary stats
      const accuracy = document.getElementById('accuracy');
      const precision = document.getElementById('precision');
      fetch('http://localhost:8080/spamDetector-1.0/api/spam/accuracy')
        .then(response => response.text())
        .then(data => {
          accuracy.textContent = data;
        });
      fetch('http://localhost:8080/spamDetector-1.0/api/spam/precision')
        .then(response => response.text())
        .then(data => {
          precision.textContent = data;
        });
    })
    .catch(error => console.error(error));
}


