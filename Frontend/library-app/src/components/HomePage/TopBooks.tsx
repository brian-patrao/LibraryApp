import { TopBook } from "./TopBook";
import { useEffect, useState } from "react";
import BookModel from "../../models/BookModel";
import { Spinner } from "../../utils/Spinner";

export const TopBooks = () => {
    const [books, setBooks] = useState<BookModel[]>([]);
    const [isloading, setIsLoading] = useState(true);
    const [errorApi, setErrorApi] = useState(null);

    useEffect(() => {
        const fetchBooks = async () => {
            const baseUrl: string = "http://localhost:8080/api/book";
            const url: string = `${baseUrl}?page=0&size=9`;

            const response = await fetch(url);

            if (!response.ok) {
                throw new Error("Something went wrong!");
            }

            const responseData = await response.json();
            const responseContent = responseData.content;
            console.log(responseContent);

            const loadedBooks: BookModel[] = [];

            for (const key in responseContent) {
                loadedBooks.push({
                    id: responseContent[key].id,
                    title: responseContent[key].title,
                    author: responseContent[key].author,
                    description: responseContent[key].description,
                    copies: responseContent[key].copies,
                    copiesAvailable: responseContent[key].copiesAvailable,
                    category: responseContent[key].category,
                    img: responseContent[key].img,
                });
            }

            setBooks(loadedBooks);
            setIsLoading(false);
        };
        fetchBooks().catch((error: any) => {
            console.log("came here");
            setIsLoading(false);
            setErrorApi(error.message);
        });
    }, []);

    if (isloading) {
        return <Spinner />;
    }

    if (errorApi) {
        <div className="container m-5">
            <p>{errorApi}</p>
        </div>;
    }

    return (
        <div className="container mt-5" style={{ height: 550 }}>
            <div className="homepage-carousel-title">
                <h3>Check out some of these books</h3>
            </div>
            <div
                id="carouselExampleControls"
                className="carousel carousel-dark slide mt-5 
                d-none d-lg-block"
                data-bs-interval="false"
            >
                <div className="carousel-inner">
                    <div className="carousel-item active">
                        <div className="row d-flex justify-content-center align-items-center">
                            {books.slice(0, 3).map((book) => (
                                <TopBook book={book} key={book.id} />
                            ))}
                        </div>
                    </div>
                    <div className="carousel-item">
                        <div className="row d-flex justify-content-center align-items-center">
                            {books.slice(3, 6).map((book) => (
                                <TopBook book={book} key={book.id} />
                            ))}
                        </div>
                    </div>
                    <div className="carousel-item">
                        <div className="row d-flex justify-content-center align-items-center">
                            {books.slice(6, 9).map((book) => (
                                <TopBook book={book} key={book.id} />
                            ))}
                        </div>
                    </div>
                    <button
                        className="carousel-control-prev"
                        type="button"
                        data-bs-target="#carouselExampleControls"
                        data-bs-slide="prev"
                    >
                        <span
                            className="carousel-control-prev-icon"
                            aria-hidden="true"
                        ></span>
                        <span className="visually-hidden">Previous</span>
                    </button>
                    <button
                        className="carousel-control-next"
                        type="button"
                        data-bs-target="#carouselExampleControls"
                        data-bs-slide="next"
                    >
                        <span
                            className="carousel-control-next-icon"
                            aria-hidden="true"
                        ></span>
                        <span className="visually-hidden">Next</span>
                    </button>
                </div>
            </div>
        </div>
    );
};
