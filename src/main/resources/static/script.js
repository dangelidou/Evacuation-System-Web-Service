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

        // Create an HTML table
        let table = `<table border='1'>
                        <tr>
                            <th>Source Node</th>
                            <th>Destination Node</th>
                            <th>Distance</th>
                            <th>Path</th>
                        </tr>`;

        // Iterate over each source node
        for (const [sourceNode, destinationMap] of Object.entries(data)) {
            for (const [destinationNode, pathData] of Object.entries(destinationMap)) {
                if (typeof pathData !== "object" || !Array.isArray(pathData.a) || !Number.isFinite(Number(pathData.b))) {
                    console.error(`Invalid path data for ${sourceNode} → ${destinationNode}:`, pathData);
                    continue; // Skip this entry
                }
                console.log(pathData.b);
                const path = pathData.a.join(" → "); // Extract path
                const distance = pathData.b.toFixed(2); // Extract distance

                table += `<tr>
                            <td>${sourceNode}</td>
                            <td>${destinationNode}</td>
                            <td>${distance}</td>
                            <td>${path}</td>
                          </tr>`;
            }
        }

        table += "</table>";

        // Display the results
        document.getElementById("shortestPaths").innerHTML = table;

    } catch (error) {
        console.error("Error while fetching shortest paths:", error);
        document.getElementById("shortestPaths").innerText = "An unexpected error occurred!";
    }
}

// Attach event listener to the form submission
document.getElementById("uploadForm").addEventListener("submit", fetchAndDisplayShortestPaths);
