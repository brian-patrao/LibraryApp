import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import { Spinner } from "../../utils/Spinner";
import { StarReview } from "../../utils/StarReviews";
import ReviewModel from "../../models/ReviewModel";
import { CheckoutAndReviewBox } from "./CheckoutAndReviewBox";
import { LatestReviews } from "./LatestReviews";
import { useOktaAuth } from "@okta/okta-react";
import ReviewRequestModel from "../../models/ReviewRequestModel";

export const BookCheckoutPage = () => {
    const { authState } = useOktaAuth();

    const [book, setBook] = useState<BookModel>();
    const [isLoading, setIsLoading] = useState(true);
    const [httpError, setHttpError] = useState(null);

    // Review State
    const [reviews, setReviews] = useState<ReviewModel[]>([]);
    const [totalStars, setTotalStars] = useState(0);
    const [isLoadingReview, setIsLoadingReview] = useState(true);

    //user has left a review
    const [isReviewLeft, setIsReviewLeft] = useState(false);
    const [isLoadingUserReview, setIsLoadingUserReview] = useState(true);

    // Loans Count State
    const [currentLoansCount, setCurrentLoansCount] = useState(0);
    const [isLoadingCurrentLoansCount, setIsLoadingCurrentLoansCount] =
        useState(true);

    // Is Book Check Out?
    const [isCheckedOut, setIsCheckedOut] = useState(false);
    const [isLoadingBookCheckedOut, setIsLoadingBookCheckedOut] =
        useState(true);

    const bookId = window.location.pathname.split("/")[2];

    useEffect(() => {
        const fetchBook = async () => {
            const baseUrl: string = `http://localhost:8080/api/book/${bookId}`;

            const response = await fetch(baseUrl);

            if (!response.ok) {
                throw new Error("Something went wrong!");
            }

            const responseJson = await response.json();

            const loadedBook: BookModel = {
                id: responseJson.id,
                title: responseJson.title,
                author: responseJson.author,
                description: responseJson.description,
                copies: responseJson.copies,
                copiesAvailable: responseJson.copiesAvailable,
                category: responseJson.category,
                img: responseJson.img,
            };

            setBook(loadedBook);
            setIsLoading(false);
        };
        fetchBook().catch((error: any) => {
            setIsLoading(false);
            setHttpError(error.message);
        });
    }, [isCheckedOut]);

    useEffect(() => {
        const fetchBookReviews = async () => {
            const reviewUrl: string = `http://localhost:8080/api/review/search/findByBookId?bookId=${bookId}`;

            const responseReviews = await fetch(reviewUrl);

            if (!responseReviews.ok) {
                throw new Error("Something went wrong!");
            }

            const responseJsonReviews = await responseReviews.json();

            const responseData = responseJsonReviews.content;

            const loadedReviews: ReviewModel[] = [];

            let weightedStarReviews: number = 0;

            for (const key in responseData) {
                loadedReviews.push({
                    id: responseData[key].id,
                    userEmail: responseData[key].email,
                    date: responseData[key].date,
                    rating: responseData[key].rating,
                    book_id: responseData[key].bookId,
                    reviewDescription: responseData[key].reviewDescr,
                });
                weightedStarReviews =
                    weightedStarReviews + responseData[key].rating;
            }

            if (loadedReviews) {
                const round = (
                    Math.round(
                        (weightedStarReviews / loadedReviews.length) * 2
                    ) / 2
                ).toFixed(1);
                setTotalStars(Number(round));
            }

            setReviews(loadedReviews);
            setIsLoadingReview(false);
        };

        fetchBookReviews().catch((error: any) => {
            setIsLoadingReview(false);
            setHttpError(error.message);
        });
    }, [isReviewLeft]);

    useEffect(() => {
        const fetchUserReviewBook = async () => {
            if (authState && authState.isAuthenticated) {
                const url = `http://localhost:8080/api/review/reviewBookByUser?bookId=${bookId}&userEmail=${authState.idToken?.claims.email}`;
                const userReview = await fetch(url);
                if (!userReview.ok) {
                    throw new Error("Something went wrong");
                }
                const userReviewResponseJson = await userReview.json();
                setIsReviewLeft(userReviewResponseJson);
            }
            setIsLoadingUserReview(false);
        };
        fetchUserReviewBook().catch((error: any) => {
            setIsLoadingUserReview(false);
            setHttpError(error.message);
        });
    }, [authState]);

    useEffect(() => {
        const fetchUserCurrentLoansCount = async () => {
            if (authState && authState.isAuthenticated) {
                const loanUrl: string = `http://localhost:8080/api/checkout/count?userEmail=${authState.idToken?.claims.email}`;
                const currentLoansCountResponse = await fetch(loanUrl);
                if (!currentLoansCountResponse.ok) {
                    throw new Error("Something went wrong!");
                }
                const currentLoansCountResponseJson =
                    await currentLoansCountResponse.json();
                setCurrentLoansCount(currentLoansCountResponseJson);
            }
            setIsLoadingCurrentLoansCount(false);
        };
        fetchUserCurrentLoansCount().catch((error: any) => {
            setIsLoadingCurrentLoansCount(false);
            setHttpError(error.message);
        });
    }, [authState, isCheckedOut]);

    useEffect(() => {
        const fetchUserCheckedOutBook = async () => {
            if (authState && authState.isAuthenticated) {
                const url = `http://localhost:8080/api/checkout/checkedOutBy?bookId=${bookId}&userEmail=${authState.idToken?.claims.email}`;
                const bookCheckedOut = await fetch(url);

                if (!bookCheckedOut.ok) {
                    throw new Error("Something went wrong!");
                }

                const bookCheckedOutResponseJson = await bookCheckedOut.json();
                setIsCheckedOut(bookCheckedOutResponseJson);
            }
            setIsLoadingBookCheckedOut(false);
        };
        fetchUserCheckedOutBook().catch((error: any) => {
            setIsLoadingBookCheckedOut(false);
            setHttpError(error.message);
        });
    }, [authState]);

    if (
        isLoading ||
        isLoadingReview ||
        isLoadingCurrentLoansCount ||
        isLoadingBookCheckedOut ||
        isLoadingUserReview
    ) {
        return <Spinner />;
    }

    if (httpError) {
        return (
            <div className="container m-5">
                <p>{httpError}</p>
            </div>
        );
    }

    async function checkoutBook() {
        if (authState?.isAuthenticated) {
            const url = `http://localhost:8080/api/checkout?bookId=${book?.id}&userEmail=${authState?.idToken?.claims.email}`;
            const requestOptions = {
                method: "PUT",
            };
            const checkoutResponse = await fetch(url, requestOptions);
            if (!checkoutResponse.ok) {
                throw new Error("Something went wrong!");
            }
            setIsCheckedOut(true);
        }
    }

    async function submitReview(starInput: number, reviewDescription: string) {
        let bookId: number = 0;
        if (book?.id) {
            bookId = book.id;
        }

        const reviewRequestModel = new ReviewRequestModel(
            starInput,
            bookId,
            reviewDescription
        );
        const url = `http://localhost:8080/api/review?userEmail=${authState?.idToken?.claims.email}`;
        const requestOptions = {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(reviewRequestModel),
        };
        const returnResponse = await fetch(url, requestOptions);
        if (!returnResponse.ok) {
            throw new Error("Something went wrong!");
        }
        setIsReviewLeft(true);
    }

    return (
        <div>
            <div className="container d-none d-lg-block">
                <div className="row mt-5">
                    <div className="col-sm-2 col-md-2">
                        <img
                            src={require("./../../Images/PublicImages/static-book-image.jpg")}
                            width="226"
                            height="349"
                            alt="Book"
                        />
                    </div>
                    <div className="col-4 col-md-4 container">
                        <div className="ml-2">
                            <h2>{book?.title}</h2>
                            <h5 className="text-primary">{book?.author}</h5>
                            <p className="lead">{book?.description}</p>
                            <StarReview rating={totalStars} size={32} />
                        </div>
                    </div>
                    <CheckoutAndReviewBox
                        book={book}
                        currentLoansCount={currentLoansCount}
                        isAuthenticated={authState?.isAuthenticated}
                        isCheckedOut={isCheckedOut}
                        checkoutBook={checkoutBook}
                        isReviewLeft={isReviewLeft}
                        submitReview={submitReview}
                    />
                </div>
                <hr />
            </div>
            <LatestReviews reviews={reviews} bookId={book?.id} />
        </div>
    );
};
