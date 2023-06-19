import LoadingTableSkeleton from "@/components/LoadingTableSkeleton";

export default function Loading() {
    return <LoadingTableSkeleton nrows={6} cellsWidth={[140, 300, 140, 200]} cellsWidthMeasure="px" />;
}
