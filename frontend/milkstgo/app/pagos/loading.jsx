import LoadingTableSkeleton from "@/components/LoadingTableSkeleton";

export default function Loading() {
    return <LoadingTableSkeleton nrows={5} cellsWidth={[25, 85, 220, 185, 225, 125, 125]} cellsWidthMeasure="px" />;
}
