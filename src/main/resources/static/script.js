async function fetchAndDisplayShortestPaths(event) {
    event.preventDefault(); // Prevent default form submission behavior

    try {
        const fileInput = document.getElementById("fileInput");
        if (!fileInput.files.length) {
            document.getElementById("shortestPaths").innerText = "Please select a file to upload.";
            return;
        }

        const formData = new FormData();
        formData.append("file", fileInput.files[0]); // Append the selected file

        const response = await fetch("/api/dijkstra/upload", { method: "POST", body: formData });

        if (!response.ok) {
            console.error("Error:", response.status, response.statusText);
            document.getElementById("shortestPaths").innerText =
                response.status === 404 ? "Network data is not available!" : "Error fetching shortest paths!";
            return;
        }

        const data = await response.json();

        if (!data || Object.keys(data).length === 0) {
            document.getElementById("shortestPaths").innerText = "No data received from the server!";
            return;
        }

        let pathsTable = buildPathsTable(data[0]);
        document.getElementById("shortestPaths").innerHTML = pathsTable;

        let disabilitiesTableTitle = "<h2>Shortest Paths for individuals with Disabilities</h2>";
        document.getElementById("shortestPathsDisabilityHeader").innerHTML = disabilitiesTableTitle;
        
        let disabilityPathsHtml;
        if (Object.keys(data[1]).length === 0)
            disabilityPathsHtml = "No data available for individuals with disabilities";
        else {
            let disabilityPathsTable = buildPathsTable(data[1]);
            disabilityPathsHtml = disabilityPathsTable;
        }

        
        
        document.getElementById("shortestDisabilityPaths").innerHTML = disabilityPathsHtml;

    } catch (error) {
        console.error("Error while fetching shortest paths:", error);
        document.getElementById("shortestPaths").innerText = "An unexpected error occurred!";
    }
}

function buildPathsTable(shortestPaths) {
    let table = `<table border='1'>
                    <tr>
                        <th>Source Node</th>
                        <th>Destination Node</th>
                        <th>Time</th>
                        <th>Path</th>
                    </tr>`;

    for (const [sourceNode, destinationMap] of Object.entries(shortestPaths)) {
        for (const [destinationNode, pathData] of Object.entries(destinationMap)) {
            var path, distance;
            
            if (typeof pathData !== "object" || !Array.isArray(pathData.a) || !Number.isFinite(Number(pathData.b))) {
                path = "Path not found"; 
                distance = "N/A"; 
            } else { 
                path = pathData.a.join(" → "); // Extract path
                distance = pathData.b.toFixed(2); // Extract distance
            }
            table += `<tr>
                        <td>${sourceNode}</td>
                        <td>${destinationNode}</td>
                        <td>${distance}</td>
                        <td>${path}</td>
                      </tr>`;
        }
    }

    table += "</table>";
    return table;
}
// Attach event listener to the form submission
document.getElementById("uploadForm").addEventListener("submit", fetchAndDisplayShortestPaths);
