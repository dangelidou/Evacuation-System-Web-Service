async function fetchAndDisplayShortestPaths() {
    try {
        const response = await fetch("/api/dijkstra/default", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({}) // Send necessary request payload if required.
        });
        if (response.ok) {
            const data = await response.json(); // Parse the JSON response

            // Create an HTML table
            let table = "<table border='1'><tr><th>Source Node</th><th>Destination Node</th><th>Distance</th></tr>";

            // Iterate over the keys
            for (const [sourceNode, destinationMap] of Object.entries(data)) {
                    for (const [destinationNode, distance] of Object.entries(destinationMap)) {
                        table += `<tr>
                                    <td>${sourceNode}</td>
                                    <td>${destinationNode}</td>
                                    <td>${distance.toFixed(2)}</td>
                                  </tr>`;
                    }
            }
            table += "</table>";

            // Display the table in the result div
            document.getElementById("shortestPaths").innerHTML = table;
        } else {
            console.error("Backend responded with an error: Status", response.status);
            if (response.status === 404) {
                document.getElementById("shortestPaths").innerText = "Network data is not available!";
            } else {
                document.getElementById("shortestPaths").innerText = "Error fetching shortest paths!";
            }
        }
    } catch (error) {
        console.error("Error while fetching shortest paths:", error);
        document.getElementById("shortestPaths").innerText = "An unexpected error occurred!";
    }
}

// Call the async function
fetchAndDisplayShortestPaths();