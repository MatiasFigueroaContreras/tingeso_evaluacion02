import LoadingTableSkeleton from "@/components/LoadingTableSkeleton";

export default function Loading() {
    return <LoadingTableSkeleton nrows={6} cellsWidth={[30, 100, 235, 200, 250, 146, 146]} cellsWidthMeasure="px" />;
}
