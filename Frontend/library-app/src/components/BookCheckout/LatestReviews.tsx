import { Link } from "react-router-dom";
import ReviewModel from "../../models/ReviewModel";
import { Review } from "../../utils/Review";

export const LatestReviews: React.FC<{
    reviews: ReviewModel[];
    bookId: number | undefined;
}> = (props) => {
    return (
        <div className="row mt-5">
            <div className="col-sm-2 col-md-2">
                <h2>Latest Reviews: </h2>
            </div>
            <div className="col-sm-10 col-md-10">
                {props.reviews.length > 0 ? (
                    <>
                        {props.reviews.slice(0, 3).map((eachReview) => (
                            <Review
                                review={eachReview}
                                key={eachReview.id}
                            ></Review>
                        ))}

                        <div className="m-3">
                            <Link
                                type="button"
                                className="btn main-color btn-md text-white"
                                to={`/reviewlist/${props.bookId}`}
                            >
                                See all reviews.
                            </Link>
                        </div>
                    </>
                ) : (
                    <div className="m-3">
                        <p className="lead">
                            Currently there are no reviews for this book
                        </p>
                    </div>
                )}
            </div>
        </div>
    );
};
