const cellNumbers = []
for (let i = 1; i <= 8; i++) {
    const row = []
    for (let j = 1; j <= 8; j++) {
        row[j] = (i - 1) * 8 + (j - 1);
    }
    cellNumbers[i] = row
}

export {cellNumbers}
