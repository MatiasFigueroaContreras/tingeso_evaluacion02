import "@/styles/feedback-alert.css"

export const feedbackTypes = {
    Informative: "informative",
    Error: "error",
    Success: "success",
};

export default function FeedbackAlert({ feedback, type }) {
    return <div className={type + "-alert"}>{feedback}</div>;
}
