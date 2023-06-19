import "@/styles/loading-table-skeleton.css";

export default function LoadingTableSkeleton({ nrows = 6, cellsWidth = [100, 100, 100, 100], cellsWidthMeasure = "px" }) {
    const renderTableContent = () => {
        const tableContent = [];
        for (let row = 0; row < nrows; row++) {
            tableContent.push(
                <div key={row} className="table-row-skeleton">
                    {cellsWidth.map((cellWidth, index) => (
                        <div
                            key={index}
                            className="table-cell-skeleton"
                            style={{ width: cellWidth + cellsWidthMeasure }}
                        ></div>
                    ))}
                </div>
            );
        }
        return tableContent;
    };

    return (
        <div className="table-skeleton">
            <div className="table-row-skeleton">
                <div
                    className="table-cell-skeleton"
                    style={{
                        width: "100%",
                    }}
                ></div>
            </div>
            {renderTableContent()}
            
        </div>
    );
}
